using Microsoft.EntityFrameworkCore;
using Identity_Provider.Models;
using System.Threading.Tasks;

namespace Identity_Provider.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options) { }
         public DbSet<Genre> Genres { get; set; }
        public DbSet<Parametre> Parametres { get; set; }
        public DbSet<UserSession> UtilisateurSessions { get; set; }
        public DbSet<Utilisateur> Utilisateurs { get; set; }
        public DbSet<TentativeHisto> TentativeHisto { get; set; }
        public DbSet<PinHisto> PinHistos { get; set; }
        public DbSet<UserProfil> UserProfils { get; set; }
        public DbSet<EtatCompte> EtatComptes { get; set; }
        public DbSet<MdpHisto> MdpHistos { get; set; }
        public DbSet<Role> Roles { get; set; }

       
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Genre>()
                .HasKey(e => e.IdGenre);

            modelBuilder.Entity<MdpHisto>()
                .HasKey(e => e.IdMdp);

            modelBuilder.Entity<EtatCompte>()
                .HasKey(e => e.IdEtat);

            modelBuilder.Entity<Parametre>()
                .HasKey(e => e.IdParametre);
            
            modelBuilder.Entity<UserProfil>()
                .HasKey(e => e.IdProfil);
            
            modelBuilder.Entity<UserSession>()
                .HasKey(e => e.IdSession);

            modelBuilder.Entity<Utilisateur>()
                .HasKey(e => e.IdUser);

            modelBuilder.Entity<TentativeHisto>()
                .HasKey(e => e.IdTentative);  // Spécifier la clé primaire

            modelBuilder.Entity<TentativeHisto>()
                .Property(t => t.DateConnection)
                .HasConversion(
                    v => v.ToUniversalTime(),   // Convertir vers UTC à l'insertion
                    v => DateTime.SpecifyKind(v, DateTimeKind.Local)  // Spécifier que la date est en UTC lorsqu'elle est lue
                );
           
            modelBuilder.Entity<UserSession>()
                .Property(t => t.DateCreation)
                .HasConversion(
                    v => v.ToUniversalTime(),   // Convertir vers UTC à l'insertion
                    v => DateTime.SpecifyKind(v, DateTimeKind.Local)  // Spécifier que la date est en UTC lorsqu'elle est lue
                );
            
            modelBuilder.Entity<UserSession>()
                .Property(t => t.DateExpiration)
                .HasConversion(
                    v => v.ToUniversalTime(),   // Convertir vers UTC à l'insertion
                    v => DateTime.SpecifyKind(v, DateTimeKind.Local)  // Spécifier que la date est en UTC lorsqu'elle est lue
                );
                        
            modelBuilder.Entity<PinHisto>()
                .HasKey(e => e.IdPin);  // Spécifier la clé primaire
 
            modelBuilder.Entity<PinHisto>()
                .Property(p => p.DateCreation)
                .HasConversion(
                    v => v.ToUniversalTime(),  // Convertir vers UTC à l'insertion
                    v => DateTime.SpecifyKind(v, DateTimeKind.Local)  // Spécifier que la date est en UTC lorsqu'elle est lue
                );
            
            modelBuilder.Entity<PinHisto>()
                .Property(p => p.DateExpiration)
                .HasConversion(
                    v => v.ToUniversalTime(),  // Convertir vers UTC à l'insertion
                    v => DateTime.SpecifyKind(v, DateTimeKind.Local)  // Spécifier que la date est en UTC lorsqu'elle est lue
                );
            modelBuilder.Entity<MdpHisto>()
                .Property(p => p.DateModif)
                .HasConversion(
                    v => v.ToUniversalTime(),  // Convertir vers UTC à l'insertion
                    v => DateTime.SpecifyKind(v, DateTimeKind.Local)  // Spécifier que la date est en UTC lorsqu'elle est lue
                );
            
            
        }

        public async Task<bool> SaveChangesAsync()
        {
            try
            {
                await base.SaveChangesAsync();
                return true;
            }
            catch (DbUpdateException dbEx)
            {
                throw new Exception("A database error occurred while saving changes.", dbEx);
            }
            catch (Exception ex)
            {
                throw new Exception("An error occurred while saving changes.", ex);
            }
        }
    }
}