using System;
using System.Linq;
using System.Threading.Tasks;
using Identity_Provider.Data;
using Identity_Provider.Models;
using Identity_Provider.Repositories;
using System.Collections.Generic;

namespace Identity_Provider.Services
{
    public class LoginService
    {
        private readonly GenericRepository<Utilisateur> _utilisateurRepository;
        private readonly GenericRepository<PinHisto> _pinHistoRepository;
        private readonly GenericRepository<TentativeHisto> _tentativeHistoRepository;
        private readonly ParametreService _parametreService;
        private readonly PinHistoService _pinHistoService;
        private readonly EmailService _emailService;

        public LoginService(
            GenericRepository<Utilisateur> utilisateurRepository,
            GenericRepository<PinHisto> pinHistoRepository,
            GenericRepository<TentativeHisto> tentativeHistoRepository,
            ParametreService parametreService,
            PinHistoService pinHistoService,
            EmailService emailService)
        {
            _utilisateurRepository = utilisateurRepository;
            _pinHistoRepository = pinHistoRepository;
            _tentativeHistoRepository = tentativeHistoRepository;
            _parametreService = parametreService;
            _pinHistoService = pinHistoService;
            _emailService = emailService;
        }

        public async Task<Utilisateur> TryBlockUserAsync(string email, string password)
        {
            Utilisateur utilisateur = null;
            try
            {
                utilisateur = await AuthenticateAsync(email, password);
            }
            catch(Exception e)
            {
                throw new Exception(e.Message);
            }

            if (utilisateur == null)
            {
                int nombreTentatives = await _parametreService.GetNombreTentativeAsync();

                utilisateur = await _utilisateurRepository.GetAllAsync()
                    .ContinueWith(task => task.Result.SingleOrDefault(u => u.Email == email));

                if (utilisateur == null)
                {
                    throw new Exception("Utilisateur non trouvé.");
                }

                var tentatives = (await _tentativeHistoRepository.GetAllAsync())
                    .Where(t => t.IdUser == utilisateur.IdUser)
                    .OrderByDescending(t => t.DateConnection)
                    .Take(nombreTentatives)
                    .ToList();

                if (tentatives.All(t => !t.Success) && tentatives.Count == nombreTentatives)
                {
                    if (utilisateur.IdEtat != 3)
                    {
                        utilisateur.IdEtat = 3;
                        await _utilisateurRepository.UpdateAsync(utilisateur);
                    }
                }
            }
    
            return utilisateur;
        }

        public async Task<Utilisateur> AuthenticateAsync(string email, string password)
        {
            var utilisateur = await _utilisateurRepository.GetAllAsync()
                .ContinueWith(task => task.Result.SingleOrDefault(u => u.Email == email));

            if (utilisateur == null)
            {
                throw new Exception("L'email est invalide.");
            }

            bool isPasswordValid = PasswordHasher.VerifyPassword(password, utilisateur.Mdp);
            await _tentativeHistoRepository.InsertAsync(new TentativeHisto
            {
                IdUser = utilisateur.IdUser,
                Success = isPasswordValid,
                DateConnection = DateTime.Now
            });

            if (!isPasswordValid)
            {
                throw new Exception("Le mot de passe est invalide.");
            }

            return utilisateur;
        }

        public async Task SendPINEmail(Utilisateur user)
        {
            var pinHisto = await _pinHistoService.GeneratePinAsync(user.Email);
            var variables = new Dictionary<string, string>
            {
                { "UserName", user.Email },  
                { "Pin", pinHisto.Pin.ToString() },  
                { "ExpirationDate", pinHisto.DateExpiration.ToString() }
            };

            await _emailService.SendUnblockEmailAsync(user.Email, "Confirmation de PIN pour debloquer votre compte", variables);
            
        }

        public async Task<Utilisateur> ValidatePinAsync(string pin, string email)
        {
            var utilisateur = await _utilisateurRepository.GetAllAsync();
            var user = utilisateur.FirstOrDefault(u => u.Email == email);

            if (user == null)
            {
                throw new Exception("L'utilisateur n'existe pas.");
            }

            var lastPinHisto = (await _pinHistoRepository.GetAllAsync())
                .Where(p => p.Email == email)
                .OrderByDescending(p => p.DateCreation)
                .FirstOrDefault();

            if (lastPinHisto == null)
            {
                throw new Exception("Aucun PIN historique trouvé pour cet utilisateur.");
            }
            Console.WriteLine(lastPinHisto.DateExpiration+" "+DateTime.Now);
            if (DateTime.UtcNow > lastPinHisto.DateExpiration)
            {
                Console.WriteLine("Temps ecoule. "+lastPinHisto.DateExpiration+" "+DateTime.Now);
                throw new Exception("Le PIN a expiré.");
            }

            if (lastPinHisto.Pin.ToString() != pin)
            {
                await _tentativeHistoRepository.InsertAsync(new TentativeHisto
                {
                    IdUser = user.IdUser,
                    Success = false,
                    DateConnection = DateTime.Now
                });
                throw new Exception("Le PIN est incorrect.");
            }

            user.IdEtat = 1;
            await _utilisateurRepository.UpdateAsync(user);

            return user;
        }

        public async Task<Utilisateur> ExecuteLoginAsync(string email, string password)
        {
            try
            {
                var user = await TryBlockUserAsync(email, password);
                if (user == null)
                {
                    throw new Exception("Utilisateur non trouvé.");
                }
            
                if (user.IdEtat == 3)
                {
                    var allTentatives = (await _tentativeHistoRepository.GetAllAsync())
                        .Where(t => t.IdUser == user.IdUser)
                        .OrderByDescending(t => t.DateConnection)
                        .ToList();

                    var lastTentative = allTentatives.FirstOrDefault();
                
                
                    if (lastTentative != null && lastTentative.Success)
                    {
                        await SendPINEmail(user);
                    }
                }

                return user;
            }
            catch (Exception e)
            {
                throw new Exception(e.Message);
            }
            
            
        }

        public async Task<string> GetLoginMessageAsync(Utilisateur user)
        {
            var allTentatives = (await _tentativeHistoRepository.GetAllAsync())
                .Where(t => t.IdUser == user.IdUser)
                .OrderByDescending(t => t.DateConnection)
                .ToList(); 

            
            var lastTentative = allTentatives.FirstOrDefault();
            if (lastTentative == null)
            {
                return "Aucune tentative enregistrée pour cet utilisateur.";
            }

            if (user.IdEtat == 3)
            { 
                if (lastTentative.Success)
                {
                    return "Un mail contenant le code PIN a été envoyé. Veuillez confirmer pour débloquer votre compte.";
                }
                else
                {
                    return "Mot de passe échoué. Vous avez atteint la limite de tentative.";
                }
            }
            else if (user.IdEtat == 1)
            {
                if (lastTentative.Success)
                {
                    return "Connexion réussie.";
                }
                else
                {
                    return "Mot de passe échoué. Veuillez réessayer.";
                }
            }

            return "État de l'utilisateur non pris en charge.";
        }
    }
}