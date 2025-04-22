using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;
using Identity_Provider.FirestoreConverters;

namespace Identity_Provider.Models
{
    [Table("user_profile")]
    [FirestoreData]
    public class UserProfil
    {
        [Key] 
        [Column("id_profil")] 
        [FirestoreProperty]
        public int IdProfil { get; set; }
        
        [Column("nom")] 
        [FirestoreProperty]
        public string Nom { get; set; }
        
        [Column("prenom")] 
        [FirestoreProperty]
        public string Prenom { get; set; }
        
        [Column("image", TypeName = "text")] 
        [FirestoreProperty]
        public string Image { get; set; }
        
        [Column("naissance")]
        [FirestoreProperty(ConverterType = typeof(DateOnlyConverter))]
        public DateOnly Naissance { get; set; }
        
        [Column("id_genre")] 
        [FirestoreProperty]
        public int IdGenre { get; set; }
        
        [Column("id_user")] 
        [FirestoreProperty]
        public int IdUser { get; set; }

        public override bool Equals(object obj)
        {
            if (obj is UserProfil other)
            {
                return this.IdProfil == other.IdProfil &&
                    this.Nom == other.Nom &&
                    this.Prenom == other.Prenom &&
                    this.Image == other.Image &&
                    this.Naissance.Equals(other.Naissance) &&
                    this.IdGenre == other.IdGenre &&
                    this.IdUser == other.IdUser;
            }
            return false;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(IdProfil, Nom, Prenom, Image, Naissance, IdGenre, IdUser);
        }

    }
}

