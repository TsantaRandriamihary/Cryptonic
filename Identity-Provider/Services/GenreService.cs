using System.Linq.Expressions;
using Identity_Provider.Models;
using Identity_Provider.Repositories;

namespace Identity_Provider.Services;

public class GenreService
{
    private readonly GenericRepository<Genre> _repository;

    public GenreService(GenericRepository<Genre> repository)
    {
        _repository = repository;
    }

    public async Task<List<Genre>> GetAllGenreAsync()
    {
        return await _repository.GetAllAsync();
    }

    public async Task<Genre> GetGenreByIdAsync(int id)
    {
        return await _repository.GetByIdAsync(id);
    }

    public async Task<Genre> CreateGenreAsync(Genre userSession)
    {
        return await _repository.InsertAsync(userSession);
    }

    public async Task<Genre> UpdateGenreAsync(Genre userSession)
    {
        return await _repository.UpdateAsync(userSession);
    }

    public async Task<bool> DeleteGenreAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
    
    public async Task<List<Genre>> GetGenreByConditionAsync(Expression<Func<Genre, bool>> predicate)
    {
        return await _repository.GetByRequestAsync(predicate);
    }
}