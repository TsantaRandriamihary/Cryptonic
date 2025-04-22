using System.Collections.Generic;
using System.Threading.Tasks;
using Identity_Provider.Models;
using Identity_Provider.Services;
using Microsoft.AspNetCore.Mvc;

namespace Identity_Provider.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ListUserController : ControllerBase
    {
        private readonly UtilisateurService _utilisateurService;

        public ListUserController(UtilisateurService utilisateurService)
        {
            _utilisateurService = utilisateurService;
        }

        
        
        
        
        [HttpGet]
        public async Task<ActionResult<ApiResponse<List<object>>>> GetUsersWithProfiles()
        {
            var data = await _utilisateurService.GetUtilisateursWithProfilesAsync();
            return Ok(ApiResponse<List<object>>.Success(data));
        }
    }
}