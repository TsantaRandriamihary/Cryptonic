package itu.p16.crypto.model.response;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private int code;
    private String status;
    private T data;
    private String error;
    private String message;
    private Object meta;
    private LocalDateTime timestamp;

    // ✅ Constructeur par défaut
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // ✅ Constructeur avec succès
    public ApiResponse(int code, String status, T data, String message, Object meta) {
        this.code = code;
        this.status = status;
        this.data = data;
        this.message = message;
        this.meta = meta;
        this.timestamp = LocalDateTime.now();
    }

    // ✅ Constructeur avec erreur
    public ApiResponse(int code, String status, String error, String message) {
        this.code = code;
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // ✅ Méthode statique pour une réponse réussie
    public static <T> ApiResponse<T> success(T data, String message, Object meta) {
        return new ApiResponse<>(200, "success", data, message, meta);
    }

    // ✅ Méthode statique pour une erreur
    public static <T> ApiResponse<T> error(String error, String message, int code) {
        return new ApiResponse<>(code, "error", error, message);
    }

    // ✅ Getters et Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
