using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;

namespace Identity_Provider.Models
{
    [Table("genre")]
    [FirestoreData]
    public class Genre
    {
        [Key] 
        [Column("id_genre")] 
        [FirestoreProperty]
        public int IdGenre { get; set; }
        [Column("val")] 
        [FirestoreProperty]
        public string val { get; set; }
    }
}