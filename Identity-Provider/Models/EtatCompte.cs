using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;

namespace Identity_Provider.Models
{
    [Table("etat_compte")]
    [FirestoreData]
    public class EtatCompte
    {
        [Key]
        [Column("id_etat")]
        [FirestoreProperty]
        public int IdEtat { get; set; }
        
        [Column("val")]
        [FirestoreProperty]
        public string Val { get; set; }
    }
}
