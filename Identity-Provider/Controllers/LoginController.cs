using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Identity_Provider.Models;
using Identity_Provider.Services;
namespace Identity_Provider.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class LoginController : ControllerBase
    {
        private readonly LoginService _loginService;
        private readonly UserSessionService _userSessionService;
        public LoginController(LoginService loginService, UserSessionService userSessionService)
        {
            _loginService = loginService;
            _userSessionService = userSessionService;
        }
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginModel loginModel)
        {
            if (string.IsNullOrEmpty(loginModel.Email) || string.IsNullOrEmpty(loginModel.Password))
            {
                return BadRequest(ApiResponse<object>.Erreur("InvalidData", "L'email et le mot de passe sont requis.", 400));
            }
            try
            {
                var user = await _loginService.ExecuteLoginAsync(loginModel.Email, loginModel.Password);
                var message = await _loginService.GetLoginMessageAsync(user);
                var sessionId = PasswordHasher.HashPassword(user.Email);
                sessionId = sessionId.Replace("/", "_");
                var userSession = await _userSessionService.CreateUserSessionByIdUserAsync(user.IdUser, sessionId);
                var cookieOptions = new CookieOptions
                {
                    HttpOnly = true, 
                    Secure = false, 
                    SameSite = SameSiteMode.Strict, 
                    Expires = userSession.DateExpiration
                };
                Response.Cookies.Append("mySessionId", sessionId, cookieOptions);
                return Ok(ApiResponse<object>.Success(user, message));
            }
            catch (Exception ex)
            {
                string errorMessage = ex.Message switch
                {
                    "L'email est invalide." => "Utilisateur non trouvé avec l'email donné.",
                    "Le mot de passe est invalide." => "Le mot de passe est incorrect.",
                    "Le PIN a expiré." => "Le PIN a expiré.",
                    "Le PIN est incorrect." => "Le PIN est incorrect.",
                    "Utilisateur non trouvé." => "Utilisateur non trouvé",
                    _ => ex.Message
                };
                return StatusCode(500, ApiResponse<object>.Erreur("ErreurLogin", errorMessage, 500));
            }
        }
        
        
        [HttpPost("validate-pin")]
        public async Task<IActionResult> ValidatePin([FromBody] PinModel pinModel)
        {
            try
            {
                if (string.IsNullOrEmpty(pinModel.Email) || string.IsNullOrEmpty(pinModel.Pin))
                {
                    return StatusCode(500, ApiResponse<object>.Erreur(
                        "ErreurLogin",
                        "Email ou PIN manquant.",
                        500));
                }
                var user = await _loginService.ValidatePinAsync(pinModel.Pin, pinModel.Email);
                var sessionId = PasswordHasher.HashPassword(user.Email);
                sessionId = sessionId.Replace("/", "_");
                var userSession = await _userSessionService.CreateUserSessionByIdUserAsync(user.IdUser, sessionId);
                var cookieOptions = new CookieOptions
                {
                    HttpOnly = true, 
                    Secure = false, 
                    SameSite = SameSiteMode.Strict, 
                    Expires = userSession.DateExpiration
                };

                Response.Cookies.Append("mySessionId", sessionId, cookieOptions);
                return Ok(ApiResponse<object>.Success(user,"Connexion réussie."));
            }
            catch (Exception ex)
            {
                var errorMessage = $"Erreur lors de la validation du PIN : {ex.Message}";
                return StatusCode(500, ApiResponse<object>.Erreur(
                    "ErreurLogin",
                    errorMessage,
                    500));
            }
        }
    }
}