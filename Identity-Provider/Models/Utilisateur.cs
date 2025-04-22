using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;

namespace Identity_Provider.Models
{
    [Table("utilisateur")]
    [FirestoreData]
    public class Utilisateur
    {
        [Key] 
        [Column("id_user")] 
        [FirestoreProperty]
        public int IdUser { get; set; }
        
        [Column("email")] 
        [FirestoreProperty]
        public string Email { get; set; }
        
        [Column("mdp")] 
        [FirestoreProperty]
        public string Mdp { get; set; }
        
        [Column("id_etat")] 
        [FirestoreProperty]
        public int IdEtat { get; set; }
        
        [Column("id_role")] 
        [FirestoreProperty]
        public int IdRole { get; set; }
        
    }
}

