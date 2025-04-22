using System;
using System.Threading.Tasks;
using Google.Cloud.Firestore;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services
{
    public class FirestoreToPostgresSyncService
    {
        private readonly FirestoreService _firestoreService;
        private readonly GenericRepository<UserProfil> _repository;

        public FirestoreToPostgresSyncService(FirestoreService firestoreService, GenericRepository<UserProfil> repository)
        {
            _firestoreService = firestoreService;
            _repository = repository;
        }

        public void ListenForFirestoreChanges()
        {
            CollectionReference collection = _firestoreService.GetFirestoreDb().Collection("UserProfil");
            collection.Listen(snapshot =>
            {
                foreach (var change in snapshot.Changes)
                {
                    switch (change.ChangeType)
                    {
                        case DocumentChange.Type.Added:
                            Console.WriteLine("Inverse sync - Ajout détecté dans Firestore.");
                            HandleFirestoreAdd(change.Document);
                            break;
                        case DocumentChange.Type.Modified:
                            Console.WriteLine("Inverse sync - Modification détectée dans Firestore.");
                            HandleFirestoreUpdate(change.Document);
                            break;
                        case DocumentChange.Type.Removed:
                            Console.WriteLine("Inverse sync - Suppression détectée dans Firestore.");
                            HandleFirestoreDelete(change.Document.Id);
                            break;
                    }
                }
            });
        }

        private async void HandleFirestoreAdd(DocumentSnapshot doc)
        {
            try
            {
                var profilFromFirestore = doc.ConvertTo<UserProfil>();
                var existing = await _repository.GetByIdAsync(profilFromFirestore.IdProfil);
                if (existing == null)
                {
                    Console.WriteLine($"Insertion dans Postgres pour le profil {profilFromFirestore.IdProfil}");
                    await _repository.InsertAsyncWithoutFirebase(profilFromFirestore);
                }
                else
                {
                    // Mettre à jour l'entité existante déjà suivie par le DbContext
                    if (!existing.Equals(profilFromFirestore))
                    {
                        existing.Nom = profilFromFirestore.Nom;
                        existing.Prenom = profilFromFirestore.Prenom;
                        existing.Image = profilFromFirestore.Image;
                        existing.Naissance = profilFromFirestore.Naissance;
                        existing.IdGenre = profilFromFirestore.IdGenre;
                        existing.IdUser = profilFromFirestore.IdUser;

                        Console.WriteLine($"Mise à jour dans Postgres pour le profil {existing.IdProfil}");
                        await _repository.UpdateAsyncWithoutFirebase(existing);
                    }
                    else
                    {
                        Console.WriteLine("Aucune modification, le profil est inchangé.");
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erreur lors de l'insertion/mise à jour dans Postgres : {ex.Message}");
            }
        }

        private async void HandleFirestoreUpdate(DocumentSnapshot doc)
        {
            try
            {
                var profilFromFirestore = doc.ConvertTo<UserProfil>();
                var existing = await _repository.GetByIdAsync(profilFromFirestore.IdProfil);
                if (existing == null)
                {
                    Console.WriteLine($"Insertion dans Postgres pour le profil {profilFromFirestore.IdProfil}");
                    await _repository.InsertAsyncWithoutFirebase(profilFromFirestore);
                }
                else
                {
                    if (!existing.Equals(profilFromFirestore))
                    {
                        existing.Nom = profilFromFirestore.Nom;
                        existing.Prenom = profilFromFirestore.Prenom;
                        existing.Image = profilFromFirestore.Image;
                        existing.Naissance = profilFromFirestore.Naissance;
                        existing.IdGenre = profilFromFirestore.IdGenre;
                        existing.IdUser = profilFromFirestore.IdUser;

                        Console.WriteLine($"Mise à jour dans Postgres pour le profil {existing.IdProfil}");
                        await _repository.UpdateAsyncWithoutFirebase(existing);
                    }
                    else
                    {
                        Console.WriteLine("Aucune modification, le profil est inchangé.");
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erreur lors de la mise à jour dans Postgres : {ex.Message}");
            }
        }


        private async void HandleFirestoreDelete(string documentId)
        {
            try
            {
                Console.WriteLine($"Suppression dans Postgres pour le profil {documentId}");
                await _repository.DeleteAsyncWithoutFirebase(int.Parse(documentId));
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Erreur lors de la suppression dans Postgres : {ex.Message}");
            }
        }
    }
}
