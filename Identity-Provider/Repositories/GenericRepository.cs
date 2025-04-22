using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;
using Google.Cloud.Firestore;
using Identity_Provider.Data;
using Identity_Provider.Services;
using Microsoft.EntityFrameworkCore;

namespace Identity_Provider.Repositories
{
    public class GenericRepository<T> where T : class
    {
        private readonly ApplicationDbContext _context;
        private readonly FirestoreService _firestoreService;
        private readonly string _collectionName;

        public GenericRepository(ApplicationDbContext context, FirestoreService firestoreService)
        {
            _context = context;
            _firestoreService = firestoreService;
            _collectionName = typeof(T).Name;
        }

        public async Task<List<T>> GetAllAsync()
        {
            return await _context.Set<T>().ToListAsync();
        }

        public async Task<T> GetByIdAsync(object id)
        {
            return await _context.Set<T>().FindAsync(id);
        }

        public async Task<List<T>> GetByRequestAsync(Expression<Func<T, bool>> predicate)
        {
            return await _context.Set<T>().Where(predicate).ToListAsync();
        }
        
        public async Task<List<T>> GetByRequestAsync(
            Expression<Func<T, bool>> predicate,
            Func<IQueryable<T>, IOrderedQueryable<T>> orderBy = null)
        {
            IQueryable<T> query = _context.Set<T>().Where(predicate);
            
            if (orderBy != null)
            {
                query = orderBy(query);
            }

            return await query.ToListAsync();
        }

        public async Task<T> InsertAsyncWithoutFirebase(T entity)
        {
            await _context.Set<T>().AddAsync(entity);
            await _context.SaveChangesAsync();
            return entity;
        }

        public async Task<T> UpdateAsyncWithoutFirebase(T entity)
        {
            _context.Set<T>().Update(entity);
            await _context.SaveChangesAsync();
            return entity;
        }

        public async Task<bool> DeleteAsyncWithoutFirebase(object id)
        {
            var entity = await GetByIdAsync(id);
            if (entity == null) return false;

            _context.Set<T>().Remove(entity);
            await _context.SaveChangesAsync();
            return true;
        }
        
        public async Task<T> InsertAsync(T entity)
        {
            await _context.Set<T>().AddAsync(entity);
            await _context.SaveChangesAsync();

            // 🔥 Ajout dans Firestore uniquement si le document n'existe pas déjà
            if (!await FirestoreDocumentExists(entity))
            {
                await _firestoreService.InsertOrUpdateEntityAsync(entity, _collectionName, GetDocumentId);
            }

            return entity;
        }

        public async Task<T> UpdateAsync(T entity)
        {
            _context.Set<T>().Update(entity);
            await _context.SaveChangesAsync();

            // 🔥 Mise à jour Firestore uniquement si le document existe
            if (await FirestoreDocumentExists(entity))
            {
                await _firestoreService.InsertOrUpdateEntityAsync(entity, _collectionName, GetDocumentId);
            }

            return entity;
        }

        public async Task<bool> DeleteAsync(object id)
        {
            var entity = await GetByIdAsync(id);
            if (entity == null) return false;

            _context.Set<T>().Remove(entity);
            await _context.SaveChangesAsync();

            // 🔥 Supprimer également dans Firestore
            await _firestoreService.DeleteEntityAsync(entity, _collectionName, GetDocumentId);

            return true;
        }

        // 📌 Vérifie si le document existe déjà dans Firestore
        private async Task<bool> FirestoreDocumentExists(T entity)
        {
            string docId = GetDocumentId(entity);
            DocumentReference docRef = _firestoreService.GetFirestoreDb().Collection(_collectionName).Document(docId);
            DocumentSnapshot docSnapshot = await docRef.GetSnapshotAsync();
            return docSnapshot.Exists;
        }

        // 📌 Génère un ID unique pour Firestore (à adapter selon ton modèle)
        /// <summary>
        /// Extrait l'identifiant du document à partir d'une entité.
        /// Cherche d'abord une propriété "Id", sinon la première propriété se terminant par "Id".
        /// </summary>
        private string GetDocumentId(T entity)
        {
            var type = typeof(T);
            // Recherche d'une propriété nommée "Id" (insensible à la casse)
            var idProperty = type.GetProperties()
                .FirstOrDefault(p => string.Equals(p.Name, "Id", StringComparison.OrdinalIgnoreCase));

            if (idProperty == null)
            {
                // Si aucune propriété "Id" n'est trouvée, recherche la première propriété dont le nom commence par "Id"
                idProperty = type.GetProperties()
                    .FirstOrDefault(p => p.Name.StartsWith("Id", StringComparison.OrdinalIgnoreCase));
            }

            if (idProperty != null)
            {
                var value = idProperty.GetValue(entity);
                if (value != null)
                {
                    string rawId = value.ToString();
                    if (string.IsNullOrWhiteSpace(rawId))
                    {
                        throw new Exception($"L'entité {type.Name} possède une propriété d'identifiant vide. Veuillez fournir une valeur non vide pour l'ID.");
                    }
                    string identity = System.Net.WebUtility.UrlEncode(rawId);
                    
                    return identity;
                }
            }

            throw new Exception($"L'entité {type.Name} doit avoir une propriété 'Id' ou commençant par 'Id' pour être stockée dans Firestore.");
        }



    }
}
