using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;
using Identity_Provider.FirestoreConverters;

namespace Identity_Provider.Models
{
    [Table("tentative_histo")]
    [FirestoreData]
    public class TentativeHisto
    {
        [Key]
        [Column("id_tentative")]
        [FirestoreProperty]
        public int IdTentative { get; set; }


        [Column("success")]
        [FirestoreProperty]
        public bool Success { get; set; }

        [Column("date_connection")]
        [FirestoreProperty(ConverterType = typeof(DateTimeConverter))]
        public DateTime DateConnection { get; set; }

        [Column("id_user")]
        [FirestoreProperty]
        public int IdUser { get; set; } 

        [ForeignKey("IdUser")]
        [FirestoreProperty]
        public virtual Utilisateur Utilisateur { get; set; }
    }
}