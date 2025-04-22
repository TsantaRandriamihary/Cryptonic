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
    public class GenreController : ControllerBase
    {
        private readonly GenreService _service;

        public GenreController(GenreService service)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<ActionResult<ApiResponse<List<Genre>>>> GetAll()
        {
            var data = await _service.GetAllGenreAsync();
            return Ok(ApiResponse<List<Genre>>.Success(data));
        }
    }
}
