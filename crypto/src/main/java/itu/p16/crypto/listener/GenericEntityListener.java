package itu.p16.crypto.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import itu.p16.crypto.model.Identifiable;
import itu.p16.crypto.service.GenericSyncService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

@Component
public class GenericEntityListener {

    private static GenericSyncService syncService;
    private static boolean syncingFromFirestore = false;

    @Autowired
    public void init(GenericSyncService genericSyncService) {
        GenericEntityListener.syncService = genericSyncService;
    }

    public static void setSyncingFromFirestore(boolean status) {
        syncingFromFirestore = status;
    }

    @PostPersist
    @PostUpdate
    public void onPersistOrUpdate(Object entity) {
        if (syncingFromFirestore) {
            System.out.println("⏭ Ignoré : Modification venant de Firestore détectée (PostgreSQL -> Firestore désactivé)");
            return;
        }

        if (entity instanceof Identifiable) {
            String collectionName = entity.getClass().getSimpleName();
            String documentId = ((Identifiable) entity).getId();
            syncService.syncPostgresToFirestore(collectionName, documentId, entity);
        }
    }

    @PostRemove
    public void onRemove(Object entity) {
        if (syncingFromFirestore) {
            System.out.println("⏭ Ignoré : Suppression venant de Firestore détectée (PostgreSQL -> Firestore désactivé)");
            return;
        }

        if (entity instanceof Identifiable) {
            String collectionName = entity.getClass().getSimpleName();
            String documentId = ((Identifiable) entity).getId();
            syncService.deleteFromFirestore(collectionName, documentId);
        }
    }
}
