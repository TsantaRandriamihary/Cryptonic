using Microsoft.AspNetCore.Http;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using Identity_Provider.Models;
using Identity_Provider.Services;

namespace Identity_Provider.Middleware
{
    public class SessionMiddleware
    {
        private readonly RequestDelegate _next;

        public SessionMiddleware(RequestDelegate next)
        {
            _next = next;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            var serviceProvider = context.RequestServices;
            var userSessionService = serviceProvider.GetRequiredService<UserSessionService>();
            var utilisateurService = serviceProvider.GetRequiredService<UtilisateurService>();
            var sessionId = context.Request.Cookies["mySessionId"];

            var condition = false;

            if (!string.IsNullOrEmpty(sessionId))
            {
                var sessionList = await userSessionService.GetUserSessionsByConditionAsync(s => s.IdSession == sessionId);
                var session = sessionList.FirstOrDefault();

                if (session != null)
                {
                    var utilisateurList = await utilisateurService.GetUtilisateursByConditionAsync(c => c.IdUser == session.IdUser);
                    var user = utilisateurList.FirstOrDefault();

                    if (user != null)
                    {
                        context.Items["User"] = user;
                        condition = true;
                    }
                }
            }
            
            if (context.Request.Path.HasValue)
            {
                // Liste des chemins à exclure
                var excludedPaths = new[]
                {
                    "/api/login/login",
                    "/api/login/validate-pin",
                    "/api/signup/signup",
                    "/api/signup/pin-confirmation",
                    "/api/logout/logout",
                    "/api/genre"
                };
                
                if (!excludedPaths.Contains(context.Request.Path.Value.ToLower()))
                {
                    if (!condition)
                    {
                        var errorResponse = ApiResponse<string>.Erreur("Session non trouvée", "Votre session est expirée, veuillez vous reconnecter.");
                        context.Response.StatusCode = 500;
                        context.Response.ContentType = "application/json";

                        var jsonResponse = JsonSerializer.Serialize(errorResponse);
                        await context.Response.WriteAsync(jsonResponse);
                        return;
                    }
                }
            }
            
            await _next(context);
        }

    }
}
