using System.Threading.Tasks;
using Google.Apis.Auth.OAuth2;
using Google.Cloud.Firestore;
using Google.Cloud.Firestore.V1;
using Identity_Provider.Models;

namespace Identity_Provider.Services
{
    public class FirestoreService
    {
        private readonly FirestoreDb _firestoreDb;
        public FirestoreService(string credentialsPath, string projectId)
        {
            if (!File.Exists(credentialsPath))
            {
                throw new FileNotFoundException($"Le fichier de credentials Firebase est introuvable : {credentialsPath}");
            }
            GoogleCredential credential = GoogleCredential.FromFile(credentialsPath);
            FirestoreClientBuilder clientBuilder = new FirestoreClientBuilder
            {
                Credential = credential
            };
            FirestoreClient client = clientBuilder.Build();
            _firestoreDb = FirestoreDb.Create(projectId, client);
        }
        
        // Pour permettre d’accéder directement à l’instance FirestoreDb (nécessaire pour installer un listener)
        public FirestoreDb GetFirestoreDb() => _firestoreDb;
        
        public async Task ClearCollectionAsync(string collectionName)
        {
            CollectionReference collection = _firestoreDb.Collection(collectionName);
            QuerySnapshot snapshot = await collection.GetSnapshotAsync();
            foreach (DocumentSnapshot doc in snapshot.Documents)
            {
                await doc.Reference.DeleteAsync();
            }
        }

        public async Task<bool> CheckIfDocumentExistsAsync(string collectionName, string documentId)
        {
            DocumentReference docRef = _firestoreDb.Collection(collectionName).Document(documentId);
            DocumentSnapshot docSnapshot = await docRef.GetSnapshotAsync();
            return docSnapshot.Exists;
        }
   

        public void ListenForChanges<T>(T entity, string collectionName, Func<T, string> getDocumentId)
        {
            CollectionReference collection = _firestoreDb.Collection(collectionName); // Correction ici
            
            // Crée un listener pour les ajouts, mises à jour ou suppressions de documents
            collection.Listen(snapshot =>
            {
                foreach (var documentChange in snapshot.Changes)
                {
                    switch (documentChange.ChangeType)
                    {
                        case Google.Cloud.Firestore.DocumentChange.Type.Added:
                            // Traitement de l'ajout de données dans Firestore
                            Console.WriteLine("Ajout");
                            HandleFirestoreAdd(documentChange.Document, entity, collectionName, getDocumentId);
                            break;
                        case Google.Cloud.Firestore.DocumentChange.Type.Modified:
                            Console.WriteLine("Modifier");
                            // Traitement de la modification de données dans Firestore
                            HandleFirestoreUpdate(documentChange.Document, entity, collectionName, getDocumentId);
                            break;
                        case Google.Cloud.Firestore.DocumentChange.Type.Removed:
                            Console.WriteLine("Supprimer");
                            // Traitement de la suppression de données dans Firestore
                            HandleFirestoreDelete(entity, collectionName, getDocumentId);
                            break;
                    }
                }
            });  // Le point-virgule était manquant ici
        }

        private async Task HandleFirestoreAdd<T>(DocumentSnapshot doc, T entity, string collectionName, Func<T, string> getDocumentId)
        {
            var modify = doc.ConvertTo<T>();
            if (await UnchangedInFirestoreAsync(entity, collectionName, getDocumentId))
            {
                Console.WriteLine($"Aucune modification, le {entity.GetType().Name} est inchangé.");
                return;
            }
            await InsertOrUpdateEntityAsync(entity, collectionName, getDocumentId);
        }

        private async Task HandleFirestoreUpdate<T>(DocumentSnapshot doc, T entity, string collectionName, Func<T, string> getDocumentId)
        {
            var modify = doc.ConvertTo<T>();
            if (await UnchangedInFirestoreAsync(entity, collectionName, getDocumentId))
            {
                Console.WriteLine($"Aucune modification, le {entity.GetType().Name} est inchangé.");
                return;
            }
            await InsertOrUpdateEntityAsync(entity, collectionName, getDocumentId);
        }

        private async Task<bool> UnchangedInFirestoreAsync<T>(T entity, string collectionName, Func<T, string> getDocumentId)
        {
            string docId = getDocumentId(entity);
            DocumentReference docRef = _firestoreDb.Collection(collectionName).Document(docId);
            DocumentSnapshot docSnapshot = await docRef.GetSnapshotAsync();
            
            if (docSnapshot.Exists)
            {
                var existing = docSnapshot.ConvertTo<T>();
                return existing.Equals(entity);
            }
            return false;
        }

        private async Task HandleFirestoreDelete<T>(T entity, string collectionName, Func<T, string> getDocumentId)
        {
            await DeleteEntityAsync(entity, collectionName, getDocumentId);
        }

        
        /// <summary>
        /// Méthode générique pour insérer ou mettre à jour une entité dans une collection Firestore.
        /// getDocumentId est une fonction qui retourne l'ID (string) du document pour l'entité.
        /// </summary>
        public async Task InsertOrUpdateEntityAsync<T>(T entity, string collectionName, Func<T, string> getDocumentId)
        {
            string docId = getDocumentId(entity);
            DocumentReference docRef = _firestoreDb.Collection(collectionName).Document(docId);
            await docRef.SetAsync(entity, SetOptions.Overwrite);
        }

        public async Task DeleteEntityAsync<T>(T entity, string collectionName, Func<T, string> getDocumentId) {
            string docId = getDocumentId(entity);
            DocumentReference docRef = _firestoreDb.Collection(collectionName).Document(docId);
            await docRef.DeleteAsync();

        }    
    }
}
