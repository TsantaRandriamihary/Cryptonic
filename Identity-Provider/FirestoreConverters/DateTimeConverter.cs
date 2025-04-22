using System;
using Google.Cloud.Firestore;

namespace Identity_Provider.FirestoreConverters
{
    public class DateTimeConverter : IFirestoreConverter<DateTime>
    {
        public object ToFirestore(DateTime value)
        {
            // Si nécessaire, ajouter 1 jour :
            // value = value.AddDays(1);

            // S'assurer que le DateTime est en UTC.
            if (value.Kind != DateTimeKind.Utc)
            {
                value = value.ToUniversalTime();
            }
            return Timestamp.FromDateTime(value);
        }

        public DateTime FromFirestore(object value)
        {
            if (value is Timestamp timestamp)
            {
                // Convertir le Timestamp en DateTime en conservant l'information UTC
                DateTime dt = timestamp.ToDateTime();

                // Si nécessaire, ajouter 1 jour lors de la lecture :
                // dt = dt.AddDays(1);

                return dt;
            }
            throw new ArgumentException("Invalid Firestore value for DateTime.");
        }
    }
}
