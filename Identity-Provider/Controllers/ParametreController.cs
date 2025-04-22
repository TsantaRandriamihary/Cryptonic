using System.Collections.Generic;
using System.Threading.Tasks;
using Identity_Provider.Models;
using Identity_Provider.Repositories;
using Identity_Provider.Services;
using Microsoft.AspNetCore.Mvc;

namespace Identity_Provider.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ParametreController : ControllerBase
    {
        private readonly ParametreService _service;

        public ParametreController(ParametreService service)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<ActionResult<ApiResponse<List<Parametre>>>> GetAll()
        {
            var data = await _service.GetAllParametresAsync();
            return Ok(ApiResponse<List<Parametre>>.Success(data));
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ApiResponse<Parametre>>> GetById(int id)
        {
            var data = await _service.GetParametreByIdAsync(id);
            if (data == null)
                return NotFound(ApiResponse<Parametre>.Erreur("Paramètre introuvable"));

            return Ok(ApiResponse<Parametre>.Success(data));
        }

        [HttpPost]
        public async Task<ActionResult<ApiResponse<Parametre>>> Create([FromBody] Parametre parametre)
        {
            var data = await _service.CreateParametreAsync(parametre);
            return CreatedAtAction(nameof(GetById), new { id = data.IdParametre }, ApiResponse<Parametre>.Success(data));
        }

        [HttpPut("{id}")]
        public async Task<ActionResult<ApiResponse<Parametre>>> Update(int id, [FromBody] Parametre parametre)
        {
            if (id != parametre.IdParametre)
                return StatusCode(400, ApiResponse<Parametre>.Erreur("L'ID de l'objet ne correspond pas à l'ID de la requête"));
            var data = await _service.UpdateParametreAsync(parametre);
            if (data == null)
                return StatusCode(400, ApiResponse<UserProfil>.Erreur("Paramètre introuvable"));
            return Ok(ApiResponse<Parametre>.Success(data));
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult<ApiResponse<bool>>> Delete(int id)
        {
            var success = await _service.DeleteParametreAsync(id);
            if (!success)
                return StatusCode(400, ApiResponse<Parametre>.Erreur("Paramètre introuvable"));

            return Ok(ApiResponse<bool>.Success(success));
        }
    }
}
