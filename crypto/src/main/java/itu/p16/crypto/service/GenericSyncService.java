package itu.p16.crypto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.Date;
import itu.p16.crypto.model.Identifiable;
import itu.p16.crypto.model.commission.Commission;
import itu.p16.crypto.model.crypto.*;
import itu.p16.crypto.model.favori.Favori;
import itu.p16.crypto.model.transaction.DemandeTransaction;
import itu.p16.crypto.model.util.FirestoreUtils;
import itu.p16.crypto.repository.commission.CommissionRepository;
import itu.p16.crypto.repository.crypto.*;
import itu.p16.crypto.repository.favori.FavoriRepository;
import itu.p16.crypto.repository.transaction.DemandeTransactionRepository;
import itu.p16.crypto.repository.transaction.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.cloud.Timestamp;
import java.util.Date; 

@Service
public class GenericSyncService {

    @Autowired
    private CryptoRepository cryptoRepository;
    @Autowired
    private MvtCryptoRepository mvtCryptoRepository;
    @Autowired
    private CryptoHistoRepository cryptoHistoRepository;
    @Autowired
    private DemandeTransactionRepository demandeTransactionRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private FavoriRepository favoriRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostConstruct
    public void init() {
        System.out.println("🚀 Initialisation de la synchronisation...");
        clearFirestoreData();
        synchronizeExistingData();
        initFirestoreListeners();
    }

    public void synchronizeExistingData() {
        System.out.println("Démarrage de la synchronisation initiale des données...");
        synchronizeCollection("Crypto", cryptoRepository.findAll());
        synchronizeCollection("MvtCrypto", mvtCryptoRepository.findAll());
        synchronizeCollection("CryptoHisto", cryptoHistoRepository.findAll());
        synchronizeCollection("DemandeTransaction", demandeTransactionRepository.findAll());
        synchronizeCollection("Transaction", transactionRepository.findAll());
        synchronizeCollection("Commission", commissionRepository.findAll());
        synchronizeCollection("Favori", favoriRepository.findAll());

        System.out.println("Synchronisation initiale terminée.");
    }

    private <T extends Identifiable> void synchronizeCollection(String collectionName, List<T> entities) {
        for (T entity : entities) {
            syncPostgresToFirestore(collectionName, entity.getId(), entity);
        }
    }

    @Transactional
    public <T> void syncPostgresToFirestore(String collectionName, String documentId, T data) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(collectionName).document(documentId);

        Map<String, Object> dataMap = objectMapper.convertValue(data, Map.class);
        dataMap.put("updatedBy", "postgres");

        // ✅ Parcourir toutes les entrées pour convertir les valeurs de type Long en Timestamp
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            if (entry.getValue() instanceof Long) {
                long millis = (Long) entry.getValue();
                dataMap.put(entry.getKey(), Timestamp.of(new Date(millis))); // ✅ Conversion en Firestore Timestamp
            }
        }

        try {
            ApiFuture<WriteResult> future = docRef.set(dataMap);
            System.out.println("✅ Sync PostgreSQL -> Firestore: " + future.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Transactional
    public void deleteFromFirestore(String collectionName, String documentId) {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection(collectionName)
                                               .document(documentId)
                                               .delete();
        try {
            System.out.println("Supprimé de Firestore: " + writeResult.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void syncFirestoreToPostgres(String collectionName, String documentId, Map<String, Object> data) {
        if ("postgres".equals(data.get("updatedBy"))) {
            System.out.println("⏭ Ignoré : Modification venant de PostgreSQL");
            return;
        }

        data.remove("updatedBy");


            switch (collectionName) {
                case "Favori":
                    System.out.println("🔥 Début sync Firestore -> PostgreSQL : " + collectionName);
                    Favori favori = objectMapper.convertValue(data, Favori.class);

                    if (documentId.matches("\\d+")) {  
                        favori.setIdFavori(Integer.parseInt(documentId));
                        favoriRepository.save(favori);
                    } else {  
                        favori.setIdFavori(null);
                        Favori savedFavori = favoriRepository.save(favori);
                        updateFirestoreDocument(collectionName, documentId, savedFavori.getIdFavori());
                    }
                    break;
                case "DemandeTransaction":
                    System.out.println("🔥 Début sync Firestore -> PostgreSQL : " + collectionName);
                    
                    DemandeTransaction demandeTransaction = new DemandeTransaction();
                    System.out.println("🔥 ATRETO ALOHA METY ");
                
                    // ✅ Assignation des valeurs manuellement
                    demandeTransaction.setIdDemande(documentId.matches("\\d+") ? Integer.parseInt(documentId) : null);
                    System.out.println("🔥 mBOLA METY ");
                    System.out.println("🔥 ITO LE TSY METY : "+data.get("idUser"));
                    System.out.println("🔥 NY TYPE ANLE TSY METY : "+data.get("idUser").getClass());
                    demandeTransaction.setIdUser(((Number) data.get("idUser")).intValue());
                    System.out.println("🔥 YEEE O ");
                    demandeTransaction.setMontantDepot(((Number) data.get("montantDepot")).doubleValue()); // Convertit en Double
                    System.out.println("🔥 METY BE ");
                    demandeTransaction.setMontantRetrait(((Number) data.get("montantRetrait")).doubleValue()); // Convertit en Double
                    System.out.println("🔥 MBOLA METY FOANA E ");
                    if (data.containsKey("dateCreation") && data.get("dateCreation") instanceof Timestamp) {
                        Timestamp firestoreTimestamp = (Timestamp) data.get("dateCreation");
                        Date date = firestoreTimestamp.toDate();
                        demandeTransaction.setDateCreation(new java.sql.Timestamp(date.getTime())); // ✅ Utilisation du chemin complet
                    }
                    System.out.println("🔥 METY ETO LE PROBLEME ");
                    // ✅ Enregistrement dans PostgreSQL
                    if (demandeTransaction.getIdDemande() != null) {
                        demandeTransactionRepository.save(demandeTransaction);
                    } else {
                        DemandeTransaction saved = demandeTransactionRepository.save(demandeTransaction);
                        updateFirestoreDocument(collectionName, documentId, saved.getIdDemande());
                    }
                    break;
                
                default:
                    System.out.println("⏭ Collection ignorée : " + collectionName);
            }
        
    }




    public void updateFirestoreDocument(String collectionName, String oldDocumentId, Integer newId) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference oldDocRef = db.collection(collectionName).document(oldDocumentId);
        DocumentReference newDocRef = db.collection(collectionName).document(String.valueOf(newId));
    
        try {
            Map<String, Object> data = oldDocRef.get().get().getData();
            if (data != null) {
                data.put("updatedBy", "firestore");
    
                if (collectionName.equals("Favori")) {
                    data.put("idFavori", String.valueOf(newId));
                }
                if (collectionName.equals("DemandeTransaction")) {
                    data.put("idDemande", String.valueOf(newId));
                }

                newDocRef.set(data).get();
                System.out.println("✅ Firestore mis à jour avec l'ID PostgreSQL : " + newId);
                oldDocRef.delete().get();
                System.out.println("🗑️ Ancien document Firestore supprimé : " + oldDocumentId);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la mise à jour de Firestore : " + e.getMessage());
        } finally {
        }
    }
    

    public void deleteFromPostgres(String collectionName, String documentId) {
        int id = Integer.parseInt(documentId);
        switch (collectionName) {
            case "DemandeTransaction":
                demandeTransactionRepository.deleteById(id);
                break;
            case "Favori":
                favoriRepository.deleteById(id);
                break;
            default:
                System.out.println("❌ Suppression ignorée pour collection inconnue : " + collectionName);
        }
    }

    @PostConstruct
    public void initFirestoreListeners() {
        FirestoreClient.getFirestore();
        listenToCollection("Crypto");
        listenToCollection("MvtCrypto");
        listenToCollection("CryptoHisto");
        listenToCollection("DemandeTransaction");
        listenToCollection("Transaction");
        listenToCollection("Commission");
        listenToCollection("Favori");
    }

    private Set<String> syncingDocuments = new HashSet<>(); // Liste des documents en cours de synchronisation

@Transactional
private void listenToCollection(String collectionName) {
    Firestore db = FirestoreClient.getFirestore();
    db.collection(collectionName)
        .addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                System.err.println("❌ Erreur Firestore : " + e.getMessage());
                return;
            }
            

            // Ignorer les collections qui ne sont pas concernées
            if (!collectionName.equals("DemandeTransaction") && !collectionName.equals("Favori")) {
                System.out.println("⏭ Ignoré : La collection " + collectionName + " n'est pas synchronisée depuis Firestore vers PostgreSQL.");
                return;
            }

            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                String docId = dc.getDocument().getId();
                Map<String, Object> data = dc.getDocument().getData();

                
                

                // Si le document est déjà en cours de synchronisation, ignorer cette synchronisation
                if (syncingDocuments.contains(docId)) {
                    System.out.println("⏭ Ignoré : Le document " + docId + " est déjà en cours de synchronisation.");
                    continue;
                }

                // Ajouter le document à la liste des documents en cours de synchronisation
                syncingDocuments.add(docId);

                // Traiter uniquement les ajouts et modifications
                switch (dc.getType()) {
                    case ADDED:
                        System.out.println("🔥 INSERT Sync Firestore -> PostgreSQL: " + collectionName + " (ID: " + docId + ")");
                        // Sync Firestore -> PostgreSQL (ajouté)
                        syncFirestoreToPostgres(collectionName, docId, data);
                        break;
                    case MODIFIED:
                        System.out.println("🔄 UPDATE Sync Firestore -> PostgreSQL: " + collectionName + " (ID: " + docId + ")");
                        // Sync Firestore -> PostgreSQL (mise à jour)
                        syncFirestoreToPostgres(collectionName, docId, data);
                        break;
                    case REMOVED:
                        System.out.println("🔄 REMOVE Sync Firestore -> PostgreSQL: " + collectionName + " (ID: " + docId + ")");
                        // Sync Firestore -> PostgreSQL (mise à jour)
                        deleteFromPostgres(collectionName, docId);
                        break;
                }

                // Après la synchronisation, retirer le document de la liste
                syncingDocuments.remove(docId);
            }
        });
}
    public void clearFirestoreData() {
        Firestore db = FirestoreClient.getFirestore();
        List<String> collections = Arrays.asList(
            "Crypto",
            "MvtCrypto",
            "CryptoHisto",
            "DemandeTransaction",
            "Commission",
            "Favori",
            "Transaction"
        );
    
        System.out.println("Effacement des données existantes dans Firestore...");
    
        for (String collectionName : collections) {
            deleteCollection(db.collection(collectionName), 100);
        }
    
        System.out.println("Effacement des données Firestore terminé.");
    }
    
    private void deleteCollection(CollectionReference collection, int batchSize) {
        try {
            ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
            int deleted = 0;
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
                ++deleted;
            }
            if (deleted >= batchSize) {
                deleteCollection(collection, batchSize);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de la collection " + collection.getId() + " : " + e.getMessage());
        }
    }
}
