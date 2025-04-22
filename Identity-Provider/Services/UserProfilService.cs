using System.Linq.Expressions;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services;

public class UserProfilService
{
    private readonly GenericRepository<UserProfil> _repository;

    public UserProfilService(GenericRepository<UserProfil> repository)
    {
        _repository = repository;
    }

    public async Task<List<UserProfil>> GetAllUserProfilAsync()
    {
        return await _repository.GetAllAsync();
    }

    public async Task<UserProfil> GetUserProfilByIdAsync(int id)
    {
        return await _repository.GetByIdAsync(id);
    }

    public async Task<UserProfil> CreateUserProfilAsync(UserProfil userSession)
    {
        return await _repository.InsertAsync(userSession);
    }

    public async Task<UserProfil> UpdateUserProfilAsync(UserProfil userSession)
    {
        return await _repository.UpdateAsync(userSession);
    }

    public async Task<bool> DeleteUserProfilAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
    
    public async Task<List<UserProfil>> GetUserProfilByConditionAsync(Expression<Func<UserProfil, bool>> predicate)
    {
        return await _repository.GetByRequestAsync(predicate);
    }
}