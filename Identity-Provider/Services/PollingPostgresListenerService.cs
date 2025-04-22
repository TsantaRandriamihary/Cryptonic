using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services
{
    // T doit être une classe (référence)
    public class PollingPostgresListenerService<T> where T : class
    {
        private readonly GenericRepository<T> _repository;
        private List<T> _cachedEntities;

        // Événement déclenché en cas de changement détecté
        public event EventHandler<List<T>> OnEntitiesChanged;

        // Fonction qui extrait une clé (par exemple, un identifiant int) à partir de T
        private readonly Func<T, int> _keySelector;

        public PollingPostgresListenerService(GenericRepository<T> repository, Func<T, int> keySelector)
        {
            _repository = repository;
            _cachedEntities = new List<T>();
            _keySelector = keySelector;
        }

        // Méthode de démarrage du polling
        public async Task StartPollingAsync(CancellationToken cancellationToken, int intervalInSeconds = 5)
        {
            // Chargement initial de la table
            _cachedEntities = await _repository.GetAllAsync();

            while (!cancellationToken.IsCancellationRequested)
            {
                try
                {
                    // Récupération de la dernière version des données
                    var latestEntities = await _repository.GetAllAsync();

                    if (!AreListsEqual(_cachedEntities, latestEntities))
                    {
                        // Mise à jour du cache
                        _cachedEntities = latestEntities;
                        // Déclenchement de l'événement avec la liste mise à jour
                        OnEntitiesChanged?.Invoke(this, latestEntities);
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Erreur lors du polling Postgres : " + ex.Message);
                }

                await Task.Delay(TimeSpan.FromSeconds(intervalInSeconds), cancellationToken);
            }
        }

        // Méthode de comparaison simple en utilisant _keySelector et la méthode Equals de T
        private bool AreListsEqual(List<T> list1, List<T> list2)
        {
            var ids1 = new HashSet<int>(list1.Select(_keySelector));
            var ids2 = new HashSet<int>(list2.Select(_keySelector));

            return ids1.SetEquals(ids2);
        }

    }
}
