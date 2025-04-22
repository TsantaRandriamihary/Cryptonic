package itu.p16.crypto.service.connection;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import itu.p16.crypto.model.response.ApiResponse;

@Service
public class ApiService {

    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T, R> ApiResponse<T> sendRequest(String url, HttpMethod method, R requestBody, ParameterizedTypeReference<ApiResponse<T>> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
    
        HttpEntity<R> requestEntity = new HttpEntity<>(requestBody, headers);
    
        try {
            ResponseEntity<ApiResponse<T>> response = restTemplate.exchange(
                url,
                method,
                requestEntity,
                responseType
            );
            return response.getBody();
        } 
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            String responseBody = ex.getResponseBodyAsString();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules(); 
                Map<String, Object> errorMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
                String errorMessage = (String) errorMap.getOrDefault("message", "Erreur inconnue");
                throw new Exception(errorMessage);
            } catch (Exception jsonException) {
                throw new RuntimeException(jsonException.getMessage());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Erreur inconnue lors de l'appel à l'API C#", ex);
        }
    }
    
    public <T> T handleResponse(ApiResponse<T> apiResponse) {
        if (apiResponse != null && apiResponse.getCode() == 200) {
            
            return apiResponse.getData();
        } else if (apiResponse != null) {
            throw new RuntimeException(apiResponse.getMessage());
        } else {
            throw new RuntimeException("Réponse de l'API C# vide ou invalide.");
        }
    }
}
