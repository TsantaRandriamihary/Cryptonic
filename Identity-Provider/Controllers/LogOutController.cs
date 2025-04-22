using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Identity_Provider.Models;
using Identity_Provider.Services;
namespace Identity_Provider.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class LogOutController : ControllerBase
    {
        private readonly UserSessionService _userSessionService;
        public LogOutController(UserSessionService userSessionService)
        {
            _userSessionService = userSessionService;
        }

        [HttpGet("logout")]
        public async Task<IActionResult> Logout()
        {
            Response.Cookies.Append("mySessionId", string.Empty, new CookieOptions
            {
                Expires = DateTime.UtcNow.AddDays(-100),
                HttpOnly = true,
                Secure = true,
                SameSite = SameSiteMode.Strict
            });

            return StatusCode(200, ApiResponse<object>.Success(null, "Vous-vous etes deconnecter"));
        }
    }
}