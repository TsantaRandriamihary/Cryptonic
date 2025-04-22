using System;
namespace Identity_Provider.Models;

public class ApiResponse<T>
{
    public int Code { get; set; }
    public string Status { get; set; }
    public T Data { get; set; }
    public string Error { get; set; }
    public string Message { get; set; }
    public object Meta { get; set; }
    public DateTime? Timestamp { get; set; } = DateTime.UtcNow;

    public static ApiResponse<T> Success(T data, string message = "Opération effectuer avec succès", object meta = null)
    {
        return new ApiResponse<T>
        {
            Code = 200,
            Status = "success",
            Data = data,
            Message = message,
            Meta = meta
        };
    }

    public static ApiResponse<T> Erreur(string error, string message = "Une erreur rencontrée", int code = 400)
    {
        return new ApiResponse<T>
        {
            Code = code,
            Status = "error",
            Data = default,
            Error = error,
            Message = message
        };
    }
}