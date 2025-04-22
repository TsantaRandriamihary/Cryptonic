package itu.p16.crypto.model.util;

import java.sql.Timestamp;
import java.util.Map;

public class FirestoreUtils {
    public static Timestamp convertFirestoreTimestamp(Map<String, Object> data, String fieldName) {
        if (data.containsKey(fieldName) && data.get(fieldName) instanceof Map) {
            Map<String, Object> timestampMap = (Map<String, Object>) data.get(fieldName);
            if (timestampMap.containsKey("seconds")) {
                long seconds = ((Number) timestampMap.get("seconds")).longValue();
                return new Timestamp(seconds * 1000);
            }
        }
        return null;
    }
}

