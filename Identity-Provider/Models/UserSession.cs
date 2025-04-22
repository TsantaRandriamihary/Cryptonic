using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;
using Identity_Provider.FirestoreConverters;

namespace Identity_Provider.Models
{
    [Table("user_session")]
    [FirestoreData]
    public class UserSession
    {
        [Key] 
        [Column("id_session")] 
        [FirestoreProperty]
        public string IdSession { get; set; }
        [Column("date_creation")] 
        [FirestoreProperty(ConverterType = typeof(DateTimeConverter))]
        public DateTime DateCreation { get; set; }
        
        [Column("date_expiration")] 
        [FirestoreProperty(ConverterType = typeof(DateTimeConverter))]
        public DateTime DateExpiration { get; set; }
        
        [Column("id_user")] 
        [FirestoreProperty]
        public int IdUser { get; set; }
    }
}