// File: Program.cs
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;
using Identity_Provider.Middleware;
using Identity_Provider.Models;
using Identity_Provider.Data;
using Identity_Provider.Repositories;
using Identity_Provider.Services;
using Microsoft.Extensions.Hosting;
using System;

var builder = WebApplication.CreateBuilder(args);

// Configuration du DbContext (Npgsql)
builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

// Enregistrement des services MVC, Razor Pages, Session...
builder.Services.AddControllersWithViews();
builder.Services.AddRazorPages();
builder.Services.AddDistributedMemoryCache();
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});
builder.Services.AddControllers();

// Enregistrement des autres services métiers
builder.Services.AddTransient<EmailService>();
builder.Services.AddTransient<GenreService>();
builder.Services.AddTransient<LoginService>();
builder.Services.AddTransient<ParametreService>();
builder.Services.AddTransient<PasswordHasher>();
builder.Services.AddTransient<PinEmailService>();
builder.Services.AddTransient<PinHistoService>();
builder.Services.AddTransient<SignUpService>();
builder.Services.AddTransient<TentativeService>();
builder.Services.AddTransient<UserProfilService>();
builder.Services.AddTransient<UserSessionService>();
builder.Services.AddTransient<UtilisateurService>();

// Enregistrement de l'open generic GenericRepository<T>
// Remplacez l'appel AddTransient par AddScoped pour l'open generic
builder.Services.AddScoped(typeof(GenericRepository<>), typeof(GenericRepository<>));

// Configuration de Swagger
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo
    {
        Title = "Identity Provider REST API",
        Version = "v1",
        Description = "Documentation pour Identity Provider REST API",
        Contact = new OpenApiContact
        {
            Name = "Support",
            Email = "andrianantenainamahandry31@gmail.com"
        }
    });
});

// Réenregistrement de certains services pour sécurité
builder.Services.AddTransient<EmailService>();
builder.Services.AddTransient<PasswordHasher>();

// --- Injections pour la synchronisation ---

// Enregistrement de PollingPostgresListenerService pour UserProfil et Utilisateur
builder.Services.AddScoped<PollingPostgresListenerService<UserProfil>>(sp =>
{
    var repository = sp.GetRequiredService<GenericRepository<UserProfil>>();
    return new PollingPostgresListenerService<UserProfil>(repository, entity => entity.IdProfil);
});

builder.Services.AddScoped<PollingPostgresListenerService<Utilisateur>>(sp =>
{
    var repository = sp.GetRequiredService<GenericRepository<Utilisateur>>();
    return new PollingPostgresListenerService<Utilisateur>(repository, entity => entity.IdUser);
});

// Enregistrement de SynchronizationManager après les services dépendants
builder.Services.AddScoped<SynchronizationManager>();

// Récupération des paramètres Firebase depuis la configuration
string firebaseCredentialsPath = builder.Configuration["Firebase:CredentialsPath"];
string projectId = builder.Configuration["Firebase:ProjectId"];

// Enregistrement de FirestoreService en singleton
builder.Services.AddSingleton<FirestoreService>(sp =>
{
    return new FirestoreService(firebaseCredentialsPath, projectId);
});

// Enregistrement du service inverse Firestore vers Postgres
builder.Services.AddScoped<FirestoreToPostgresSyncService>();

// Enregistrement du BackgroundService pour la synchronisation
builder.Services.AddHostedService<SynchronizationBackgroundService>();

var app = builder.Build();

// Utilisation de la session
app.UseSession();

// Configuration de Swagger en développement
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI(c =>
    {
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "Identity Provider REST API v1");
    });
}
else
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseAuthentication();
app.UseAuthorization();

// Mapping des controllers et des routes classiques
app.MapControllers();
app.UseCors(policy =>
    policy.AllowAnyOrigin()
          .AllowAnyHeader()
          .AllowAnyMethod());
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

// Démarrage de l'application web
app.Run();
