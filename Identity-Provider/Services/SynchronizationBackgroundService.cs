using System;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services
{
    public class SynchronizationBackgroundService : BackgroundService
    {
        private readonly IServiceProvider _serviceProvider;

        public SynchronizationBackgroundService(IServiceProvider serviceProvider)
        {
            _serviceProvider = serviceProvider;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            try
            {
                using (var scope = _serviceProvider.CreateScope())
                {
                    // Récupérer les services nécessaires
                    var firestoreService = scope.ServiceProvider.GetRequiredService<FirestoreService>();
                    var syncManager = scope.ServiceProvider.GetRequiredService<SynchronizationManager>();
                    var userProfilRepository = scope.ServiceProvider.GetRequiredService<GenericRepository<UserProfil>>();
                    var userRepository = scope.ServiceProvider.GetRequiredService<GenericRepository<Utilisateur>>();

                    // Effectuer la synchronisation initiale depuis PostgreSQL vers Firestore
                    await syncManager.FullSyncFirestoreFromPostgresAsync(userRepository, "Utilisateur", user => user.IdUser.ToString(), async user => await firestoreService.InsertOrUpdateEntityAsync(user, "Utilisateur", u => u.IdUser.ToString()));
                    await syncManager.FullSyncFirestoreFromPostgresAsync(userProfilRepository, "UserProfil", profil => profil.IdProfil.ToString(), async profil => await firestoreService.InsertOrUpdateEntityAsync(profil, "UserProfil", p => p.IdProfil.ToString()));

                    // Configurer le listener PostgreSQL (pour écouter les changements dans PostgreSQL)
                    syncManager.SetupPostgresPollingListener();

                    var firestoreToPostgres = scope.ServiceProvider.GetRequiredService<FirestoreToPostgresSyncService>();
                    firestoreToPostgres.ListenForFirestoreChanges();

                    // Attendre indéfiniment, en maintenant le service actif
                    await Task.Delay(Timeout.Infinite, stoppingToken);
                }
            }     
            catch (Exception ex)
            {
                Console.WriteLine($"🔥 Erreur dans SynchronizationBackgroundService : {ex.Message}");
            }    
        }
    }
}
