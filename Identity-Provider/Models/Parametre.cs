using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;

namespace Identity_Provider.Models
{
    [Table("parametre")]
    [FirestoreData]
    public class Parametre
    {
        [Key] 
        [Column("id_parametre")] 
        [FirestoreProperty]
        public int IdParametre { get; set; }
        
        [Column("nbr_tentative")] 
        [FirestoreProperty]
        public int NbrTentative { get; set; }
        
        [Column("duree_tentative")] 
        [FirestoreProperty]
        public int DureeTentative { get; set; }
        
        [Column("duree_session")] 
        [FirestoreProperty]
        public int DureeSession { get; set; }
        
        [Column("duree_pin")] 
        [FirestoreProperty]
        public int DureePin { get; set; }
    }
}

