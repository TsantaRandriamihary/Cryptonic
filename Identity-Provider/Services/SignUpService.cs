using System;
using System.Linq;
using System.Threading.Tasks;
using Identity_Provider.Models;
using Identity_Provider.Repositories;
using System.Collections.Generic;

namespace Identity_Provider.Services
{
    public class SignUpService
    {
        private readonly GenericRepository<PinHisto> _pinHistoRepository;

        public SignUpService(
            GenericRepository<PinHisto> pinHistoRepository)
        {
            _pinHistoRepository = pinHistoRepository;
        }
        
        public async Task<Boolean> ValidatePinAsync(string pin, string email)
        {
            var lastPinHisto = (await _pinHistoRepository.GetAllAsync())
                .Where(p => p.Email == email)
                .OrderByDescending(p => p.DateCreation)
                .FirstOrDefault();

            if (lastPinHisto == null)
            {
                throw new Exception("Aucun PIN historique trouvé pour cet email.");
            }
            Console.WriteLine(lastPinHisto.DateExpiration+" "+DateTime.Now);
            if (DateTime.UtcNow > lastPinHisto.DateExpiration)
            {
                Console.WriteLine("Temps ecoule. "+lastPinHisto.DateExpiration+" "+DateTime.Now);
                throw new Exception("Le PIN a expiré.");
            }

            if (lastPinHisto.Pin.ToString() != pin)
            {
                throw new Exception("Le PIN est incorrect. Veuillez réessayer");
            }
            return true;
        }
    }
}