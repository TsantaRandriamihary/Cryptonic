using System;
using System.Threading.Tasks;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services
{
    public class PinHistoService
    {
        private readonly GenericRepository<PinHisto> _pinHistoRepository;
        private readonly ParametreService _parametreService;

        public PinHistoService(GenericRepository<PinHisto> pinHistoRepository, ParametreService parametreService)
        {
            _pinHistoRepository = pinHistoRepository;
            _parametreService = parametreService;
        }

        // Générer un PIN et l'enregistrer dans l'historique
        public async Task<PinHisto> GeneratePinAsync(string email)
        {
            // Récupérer la durée de validité du PIN depuis les paramètres
            int dureePin = await _parametreService.GetDureePinAsync();

            // Générer un PIN aléatoire entre 100000 et 999999
            var random = new Random();
            int pin = random.Next(100000, 1000000);

            // Définir les dates de création et d'expiration du PIN
            DateTime dateCreation = DateTime.Now;
            DateTime dateExpiration = dateCreation.AddSeconds(dureePin);

            // Créer un nouvel enregistrement de l'historique du PIN
            var pinHisto = new PinHisto
            {
                Pin = pin,
                Email = email,
                DateCreation = dateCreation,
                DateExpiration = dateExpiration
            };

            // Insérer l'enregistrement dans la base de données
            await _pinHistoRepository.InsertAsync(pinHisto);

            // Retourner l'objet PinHisto créé
            return pinHisto;
        }
    }
}