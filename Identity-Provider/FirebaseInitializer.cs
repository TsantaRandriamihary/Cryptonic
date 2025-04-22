using Google.Cloud.Firestore;
using System;
using System.IO;

namespace Identity_Provider
{
    public static class FirebaseInitializer
    {
        public static void InitializeFirebase(string credentialsPath)
        {
            if (!File.Exists(credentialsPath))
            {
                throw new FileNotFoundException($"Le fichier de credentials Firebase est introuvable : {credentialsPath}");
            }
            Environment.SetEnvironmentVariable("GOOGLE_APPLICATION_CREDENTIALS", Path.GetFullPath(credentialsPath));
        }
    }
}
