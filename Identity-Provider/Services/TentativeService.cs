using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services
{
    public class TentativeService
    {
        private readonly GenericRepository<TentativeHisto> _tentativeRepository;

        public TentativeService(GenericRepository<TentativeHisto> tentativeRepository)
        {
            _tentativeRepository = tentativeRepository;
        }

        // Récupérer les dernières tentatives pour un utilisateur avec une limite spécifiée
        public async Task<List<TentativeHisto>> GetLastTentativesAsync(int userId, int tentativeLimit)
        {
            if (tentativeLimit <= 1)
            {
                throw new ArgumentException("Le nombre limite de tentatives doit être supérieur à 1.");
            }

            // Récupérer toutes les tentatives pour un utilisateur donné
            var allTentatives = await _tentativeRepository.GetAllAsync();
            
            // Filtrer et trier les tentatives, puis appliquer la limite
            var filteredTentatives = allTentatives
                .Where(t => t.IdUser == userId)
                .OrderByDescending(t => t.DateConnection)
                .Take(tentativeLimit)
                .ToList();
            
            return filteredTentatives;
        }

        // Générer un nouvel enregistrement pour une tentative (succès ou échec)
        public async Task<TentativeHisto> GenerateTentativeHistoAsync(int idUser, bool isCorrect)
        {
            var tentativeHisto = new TentativeHisto
            {
                IdUser = idUser,
                Success = isCorrect,
                DateConnection = DateTime.Now
            };

            // Insérer la tentative dans la base de données
            await _tentativeRepository.InsertAsync(tentativeHisto);
            return tentativeHisto;
        }
    }
}