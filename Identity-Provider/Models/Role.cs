using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;

namespace Identity_Provider.Models
{
    [Table("role")]
    [FirestoreData]
    public class Role
    {
        [Key]
        [Column("id_role")]
        [FirestoreProperty]
        public int IdRole { get; set; }
        
        [Column("val")]
        [FirestoreProperty]
        public string Val { get; set; }
    }
}
