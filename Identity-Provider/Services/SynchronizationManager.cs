using Identity_Provider.Models;
using Identity_Provider.Repositories;
using System;
using System.Threading.Tasks;

namespace Identity_Provider.Services
{
    public class SynchronizationManager
    {
        private readonly FirestoreService _firestoreService;
        private readonly PollingPostgresListenerService<UserProfil> _userProfilPollingService;
        private readonly PollingPostgresListenerService<Utilisateur> _utilisateurPollingService;
        
        private bool _firestoreListenerEnabled = true;
        private bool _postgresListenerEnabled = true;
        
        public SynchronizationManager(
            FirestoreService firestoreService,
            PollingPostgresListenerService<UserProfil> userProfilPollingService,
            PollingPostgresListenerService<Utilisateur> utilisateurPollingService)
        {
            _firestoreService = firestoreService;
            _userProfilPollingService = userProfilPollingService;
            _utilisateurPollingService = utilisateurPollingService;
        }

        public void SetupPostgresPollingListener()
        {
            // Écoute les changements dans la table UserProfil
            _userProfilPollingService.OnEntitiesChanged += async (sender, latestProfiles) =>
            {
                if (!_postgresListenerEnabled) return;
                
                _firestoreListenerEnabled = false;
                try
                {
                    foreach (var profil in latestProfiles)
                    {
                        await _firestoreService.InsertOrUpdateEntityAsync(profil, "UserProfil", p => p.IdProfil.ToString());
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Erreur de synchronisation Firestore : " + ex.Message);
                }
                finally
                {
                    _firestoreListenerEnabled = true;
                }
            };


            // Écoute les changements dans la table Utilisateur
            _utilisateurPollingService.OnEntitiesChanged += async (sender, latestUsers) =>
            {
                if (!_postgresListenerEnabled) return;
                _firestoreListenerEnabled = false;
                
                try
                {    
                    foreach (var user in latestUsers)
                    {
                        await _firestoreService.InsertOrUpdateEntityAsync(user, "Utilisateur", u => u.IdUser.ToString());
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Erreur de synchronisation Firestore : " + ex.Message);
                }
                finally
                {
                    _firestoreListenerEnabled = true;
                }
            };
        }

        public async Task FullSyncFirestoreFromPostgresAsync<T>(GenericRepository<T> repository, string collectionName, Func<T, string> getDocumentId, Func<T, Task> insertOrUpdateFunc) where T : class
        {
            _firestoreListenerEnabled = false;
            await _firestoreService.ClearCollectionAsync(collectionName);
            
            var allEntities = await repository.GetAllAsync();
            foreach (var entity in allEntities)
            {
                await insertOrUpdateFunc(entity);
            }
            
            _firestoreListenerEnabled = true;
        }
    }
}
