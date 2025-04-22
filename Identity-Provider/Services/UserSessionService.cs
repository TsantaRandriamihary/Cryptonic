using System.Linq.Expressions;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services;

public class UserSessionService
{
    private readonly GenericRepository<UserSession> _repository;
    private readonly ParametreService _parametreService;
    
    public UserSessionService(GenericRepository<UserSession> repository, ParametreService parametreService)
    {
        _repository = repository;
        _parametreService = parametreService;
    }

    public async Task<List<UserSession>> GetAllUserSessionsAsync()
    {
        return await _repository.GetAllAsync();
    }

    public async Task<UserSession> GetUserSessionByIdAsync(string id)
    {
        return await _repository.GetByIdAsync(id);
    }

    public async Task<UserSession> CreateUserSessionAsync(UserSession userSession)
    {
        return await _repository.InsertAsync(userSession);
    }
    
    public async Task<UserSession> CreateUserSessionByIdUserAsync(int idUser, string sessionId)
    {
        var parametreList = await _parametreService.GetAllParametresAsync();
        var parametre = parametreList.FirstOrDefault();
        if (parametre == null)
        {
            throw new Exception("Aucun paramètre dans la base, veuillez insérer");
        }
        var date = DateTime.UtcNow;
        var userSession = new UserSession()
        {
            IdSession = sessionId,
            DateCreation = date,
            DateExpiration = date.AddSeconds(parametre.DureeSession),
            IdUser = idUser
        };
        return await _repository.InsertAsync(userSession);
    }

    public async Task<UserSession> UpdateUserSessionAsync(UserSession userSession)
    {
        return await _repository.UpdateAsync(userSession);
    }

    public async Task<bool> DeleteUserSessionAsync(string id)
    {
        return await _repository.DeleteAsync(id);
    }
    
    public async Task<List<UserSession>> GetUserSessionsByConditionAsync(Expression<Func<UserSession, bool>> predicate)
    {
        return await _repository.GetByRequestAsync(predicate, q => q.OrderByDescending(s => s.DateCreation));
    }
}