using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Identity_Provider.Models;
using Identity_Provider.Services;
using Microsoft.AspNetCore.Http.HttpResults;
namespace Identity_Provider.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class SignUpController : ControllerBase
    {
        private readonly PinEmailService _pinEmailService;
        private readonly PinHistoService _pinHistoService;
        private readonly UtilisateurService _utilisateurService;
        private readonly UserSessionService _userSessionService;
        private readonly SignUpService _signUpService;

        public SignUpController(PinEmailService emailService, PinHistoService pinHistoService, SignUpService signUpService, UserSessionService userSessionService, UtilisateurService utilisateurService)
        {
            _pinEmailService = emailService;
            _pinHistoService = pinHistoService;
            _signUpService = signUpService;
            _userSessionService = userSessionService;
            _utilisateurService = utilisateurService;
        }

        [HttpPost("signup")]
        public async Task<IActionResult> SignUp([FromBody] SignUpModel signUp)
        {
            if (string.IsNullOrEmpty(signUp.Email) || string.IsNullOrEmpty(signUp.Password))
            {
                return StatusCode(500, ApiResponse<object>.Erreur("InvalidData", "L'email et le mot de passe sont requis.", 500));
            }
            try
            {
                var pin = await _pinHistoService.GeneratePinAsync(signUp.Email);
                await _pinEmailService.SendPinTentativeEmailAsync(signUp.Email, "cher Client", pin.Pin.ToString());

                // Stocker l'email et le mot de passe dans la session
                HttpContext.Session.SetString("Session-User-Email", signUp.Email);
                HttpContext.Session.SetString("Session-User-Password", signUp.Password);

                var responseData = "Un code PIN a été envoyé sur votre email, veuillez le confirmer.";
                
                return Ok(ApiResponse<object>.Success(responseData, "Email envoyé avec succès."));
            }
            catch (Exception ex)
            {
                var errorMessage = $"Erreur lors de l'envoi du PIN par email: {ex.Message}";
                return StatusCode(500, ApiResponse<object>.Erreur(
                    "ErreurSignUp",
                    errorMessage,
                    500));
            }
        }

        [HttpPost("pin-confirmation")]
        public async Task<IActionResult> PinConfirmation([FromBody] PinConfirmationModel pinModel)
        {
            if (string.IsNullOrEmpty(pinModel.Pin))
            {
                return StatusCode(400, ApiResponse<object>.Erreur("InvalidData", "Le PIN est requis.", 400));
            }

            // Récupérer l'email et le mot de passe depuis la session
            var userEmail = pinModel.Email;
            var userPassword = pinModel.Password;

            if (string.IsNullOrEmpty(userEmail))
            {
                return StatusCode(400, ApiResponse<object>.Erreur("MissingEmail", "Email introuvable dans la session.", 400));
            }
            if (string.IsNullOrEmpty(userPassword))
            {
                return StatusCode(400, ApiResponse<object>.Erreur("MissingPassword", "Mot de passe introuvable dans la session.", 400));
            }

            try
            {
                var utilisateur = new Utilisateur()
                {
                    Email = userEmail,
                    Mdp = PasswordHasher.HashPassword(userPassword),
                    IdEtat = 1,
                    IdRole = 1
                };
                var user = await _utilisateurService.CreateUtilisateurAsync(utilisateur);
                var statut = await _signUpService.ValidatePinAsync(pinModel.Pin, userEmail);
                var message = "Code PIN incorrect. Veuillez-vous reconnecter.";
                var sessionId = PasswordHasher.HashPassword(pinModel.Pin);
                sessionId = sessionId.Replace("/", "_");
                if (statut)
                {
                    message = "Connection réussie, insérer des infos pour votre profil.";
                    var userSession = await _userSessionService.CreateUserSessionByIdUserAsync(user.IdUser, sessionId);
                    var cookieOptions = new CookieOptions
                    {
                        HttpOnly = true,
                        Secure = false,
                        SameSite = SameSiteMode.Strict,
                        Expires = userSession.DateExpiration
                    };
                    Response.Cookies.Append("mySessionId", sessionId, cookieOptions);
                }
                var responseData = user;
                return Ok(ApiResponse<object>.Success(responseData, "Authentification PIN réussie."));
            }
            catch (Exception ex)
            {
                var errorMessage = $"Erreur lors de l'authentification du PIN: {ex.Message}";
                return StatusCode(500, ApiResponse<object>.Erreur(
                    "ErreurSignUp",
                    errorMessage,
                    500));
            }
        }
    }

    
    public class SignUpModel
    {
        public string Email { get; set; }
        public string Password { get; set; }
    }
    
    public class PinConfirmationModel
    {
        public string Pin { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
    }
}