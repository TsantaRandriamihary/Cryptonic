package itu.p16.crypto.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import itu.p16.crypto.model.user.*;
import itu.p16.crypto.model.response.ApiResponse;
import itu.p16.crypto.service.connection.ApiService;

import java.util.List;

@Service
public class UtilisateurService {

    private final ApiService apiService;

    @Value("${csharp.api.base-url}")
    private String csharpApiBaseUrl;

    public UtilisateurService(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<UtilisateurWithProfil> getAllUtilisateursWithProfiles() {
        String url = csharpApiBaseUrl + "/api/ListUser"; 
        ApiResponse<List<UtilisateurWithProfil>> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<List<UtilisateurWithProfil>>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }

}
