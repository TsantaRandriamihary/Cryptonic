using System;
using Google.Cloud.Firestore;

namespace Identity_Provider.FirestoreConverters
{
    public class DateOnlyConverter : IFirestoreConverter<DateOnly>
    {
        public object ToFirestore(DateOnly value)
        {
            // value = value.AddDays(1);
            DateTime dateTime = value.ToDateTime(TimeOnly.MinValue).ToUniversalTime();
            return Timestamp.FromDateTime(dateTime);
        }

        public DateOnly FromFirestore(object value)
        {
            if (value is Timestamp timestamp)
            {
                // Convertir le Timestamp en DateTime (en ignorant l'heure) puis ajouter un jour
                DateTime dateTime = timestamp.ToDateTime().Date;
                // dateTime = dateTime.AddDays(1);
                return DateOnly.FromDateTime(dateTime);
            }
            throw new ArgumentException("Invalid Firestore value for DateOnly.");
        }
    }
}
