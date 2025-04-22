using System.Collections.Generic;
using System.Linq.Expressions;
using System.Threading.Tasks;
using Identity_Provider.Models;
using Identity_Provider.Repositories;
using Identity_Provider.Services;
using Microsoft.AspNetCore.Mvc;

namespace Identity_Provider.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UserProfilController : ControllerBase
    {
        private readonly UserProfilService _service;

        public UserProfilController(UserProfilService service)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<ActionResult<ApiResponse<List<UserProfil>>>> GetAll()
        {
            var data = await _service.GetAllUserProfilAsync();
            return Ok(ApiResponse<List<UserProfil>>.Success(data));
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ApiResponse<UserProfil>>> GetById(int id)
        {
            var data = await _service.GetUserProfilByIdAsync(id);
            if (data == null)
                return StatusCode(400, ApiResponse<UserProfil>.Erreur("Paramètre introuvable"));

            return Ok(ApiResponse<UserProfil>.Success(data));
        }

        [HttpPost]
        public async Task<ActionResult<ApiResponse<UserProfil>>> Create([FromBody] UserProfil userProfil)
        {            
            var parametre = new UserProfil()
            {
                IdUser = userProfil.IdUser,
                IdGenre = userProfil.IdGenre,
                Naissance = userProfil.Naissance,
                Nom = userProfil.Nom,
                Image = userProfil.Image,
                Prenom = userProfil.Prenom
            };
            
            var data = await _service.CreateUserProfilAsync(parametre);

            return CreatedAtAction(nameof(GetById), new { id = data.IdProfil }, ApiResponse<UserProfil>.Success(data));
        }


        [HttpPut("{id}")]
        public async Task<ActionResult<ApiResponse<UserProfil>>> Update(int id, [FromBody] UserProfil parametre)
        {
            if (id != parametre.IdProfil)
                return StatusCode(500, ApiResponse<UserProfil>.Erreur("L'ID de l'objet ne correspond pas à l'ID de la requête"));

            var data = await _service.UpdateUserProfilAsync(parametre);
            return Ok(ApiResponse<UserProfil>.Success(data));
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult<ApiResponse<bool>>> Delete(int id)
        {
            var success = await _service.DeleteUserProfilAsync(id);
            if (!success)
                return StatusCode(400, ApiResponse<UserProfil>.Erreur("Paramètre introuvable"));

            return Ok(ApiResponse<bool>.Success(success));

        }

        [HttpGet("user/{id}")]
        public async Task<ActionResult<ApiResponse<List<UserProfil>>>> GetUsersByCondition(int id)
        {
            Expression<Func<UserProfil, bool>> predicate = u => u.IdUser == id;
            var data = await _service.GetUserProfilByConditionAsync(predicate);

            return Ok(ApiResponse<List<UserProfil>>.Success(data));
        }

    }

}
