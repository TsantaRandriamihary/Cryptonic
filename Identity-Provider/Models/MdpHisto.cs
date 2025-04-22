using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Google.Cloud.Firestore;
using Identity_Provider.FirestoreConverters;

namespace Identity_Provider.Models
{
    [Table("mdp_histo")]
    [FirestoreData]
    public class MdpHisto
    {
        [Key]
        [Column("id_mdp")]
        [FirestoreProperty]
        public int IdMdp { get; set; }
        
        [Column("mdp_new")]
        [FirestoreProperty]
        public string MdpNew { get; set; }
        
        [Column("date_modif")]
        [FirestoreProperty(ConverterType = typeof(DateTimeConverter))]
        public DateTime DateModif { get; set; }
        
        [Column("id_user")]
        [FirestoreProperty]
        public int IdUser { get; set; }
    }
}
