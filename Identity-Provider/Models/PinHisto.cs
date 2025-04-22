using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;
using Identity_Provider.FirestoreConverters;

namespace Identity_Provider.Models
{
    [Table("pin_histo")]
    [FirestoreData]
    public class PinHisto
    {
        [Key]
        [Column("id_pin")]
        [FirestoreProperty]
        public int IdPin { get; set; }

        [Column("pin")]
        [FirestoreProperty]
        [Required]
        public int Pin { get; set; }

        [Column("date_creation")]  
        [FirestoreProperty(ConverterType = typeof(DateTimeConverter))]
        [Required]
        public DateTime DateCreation { get; set; }

        [Column("date_expiration")]
        [FirestoreProperty(ConverterType = typeof(DateTimeConverter))]
        public DateTime DateExpiration { get; set; } 

        [Column("email")]
        [FirestoreProperty]
        [Required]
        [StringLength(255)] 
        public string Email { get; set; }
    }
}