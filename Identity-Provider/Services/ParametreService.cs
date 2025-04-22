using System.Linq.Expressions;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services;

public class ParametreService
{
    private readonly GenericRepository<Parametre> _repository;

    public ParametreService(GenericRepository<Parametre> repository)
    {
        _repository = repository;
    }

    public async Task<List<Parametre>> GetAllParametresAsync()
    {
        return await _repository.GetAllAsync();
    }

    public async Task<Parametre> GetParametreByIdAsync(int id)
    {
        return await _repository.GetByIdAsync(id);
    }

    public async Task<Parametre> CreateParametreAsync(Parametre parametre)
    {
        return await _repository.InsertAsync(parametre);
    }

    public async Task<Parametre> UpdateParametreAsync(Parametre parametre)
    {
        return await _repository.UpdateAsync(parametre);
    }

    public async Task<bool> DeleteParametreAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
    
    public async Task<List<Parametre>> GetParametresByConditionAsync(Expression<Func<Parametre, bool>> predicate)
    {
        return await _repository.GetByRequestAsync(predicate);
    }

    // Récupérer le nombre de tentatives depuis le paramètre
    public async Task<int> GetNombreTentativeAsync()
    {
        var parametre = await _repository.GetAllAsync();
        var firstParametre = parametre.FirstOrDefault();
        if (firstParametre == null)
        {
            throw new Exception("Aucun paramètre configuré dans la base de données.");
        }
        return firstParametre.NbrTentative;
    }

    // Récupérer la durée du PIN depuis le paramètre
    public async Task<int> GetDureePinAsync()
    {
        var parametre = await _repository.GetAllAsync();
        var firstParametre = parametre.FirstOrDefault();
        if (firstParametre == null)
        {
            throw new Exception("Les paramètres n'ont pas été configurés.");
        }
        return firstParametre.DureePin;
    }
}