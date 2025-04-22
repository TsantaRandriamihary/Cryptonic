using System.Linq.Expressions;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services;

public class UtilisateurService
{
    private readonly GenericRepository<Utilisateur> _repository;
    private readonly GenericRepository<UserProfil> _userProfilRepository;

    public UtilisateurService(GenericRepository<Utilisateur> repository, GenericRepository<UserProfil> userProfilRepository)
    {
        _repository = repository;
        _userProfilRepository = userProfilRepository;
    }

    public async Task<List<Utilisateur>> GetAllUtilisateursAsync()
    {
        return await _repository.GetAllAsync();
    }

    public async Task<Utilisateur> GetUtilisateurByIdAsync(string id)
    {
        return await _repository.GetByIdAsync(id);
    }

    public async Task<Utilisateur> CreateUtilisateurAsync(Utilisateur userSession)
    {
        return await _repository.InsertAsync(userSession);
    }

    public async Task<Utilisateur> UpdateUtilisateurAsync(Utilisateur userSession)
    {
        return await _repository.UpdateAsync(userSession);
    }

    public async Task<bool> DeleteUtilisateurAsync(string id)
    {
        return await _repository.DeleteAsync(id);
    }
    
    public async Task<List<Utilisateur>> GetUtilisateursByConditionAsync(Expression<Func<Utilisateur, bool>> predicate)
    {
        return await _repository.GetByRequestAsync(predicate);
    }
    
    public async Task<List<object>> GetUtilisateursWithProfilesAsync()
    {
        var utilisateurs = await _repository.GetByRequestAsync(u => u.IdRole == 1);
        if (utilisateurs == null || utilisateurs.Count == 0)
            return new List<object>();
        var utilisateursAvecProfils = new List<object>();
        foreach (var utilisateur in utilisateurs)
        {
            var profil = await _userProfilRepository.GetByRequestAsync(p => p.IdUser == utilisateur.IdUser);
            utilisateursAvecProfils.Add(new
            {
                user = utilisateur,
                userProfil = profil.FirstOrDefault() 
            });
        }
        return utilisateursAvecProfils;
    }


}