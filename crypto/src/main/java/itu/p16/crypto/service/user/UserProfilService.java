package itu.p16.crypto.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import itu.p16.crypto.model.user.UserProfil;
import itu.p16.crypto.service.connection.ApiService;
import itu.p16.crypto.model.response.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserProfilService {

    private final ApiService apiService;

    @Value("${csharp.api.base-url}")
    private String csharpApiBaseUrl;

    public UserProfilService(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<UserProfil> getAllUserProfils() {
        String url = csharpApiBaseUrl + "/api/UserProfil";
        ApiResponse<List<UserProfil>> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<List<UserProfil>>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }

    public UserProfil getUserProfilById(int id) {
        String url = csharpApiBaseUrl + "/api/UserProfil/" + id;
        ApiResponse<UserProfil> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<UserProfil>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }

    public List<UserProfil> getUserProfilByIdUser(int id) {
        String url = csharpApiBaseUrl + "/api/UserProfil/user/" + id;
        ApiResponse<List<UserProfil>> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<List<UserProfil>>>() {}
        );
        List<UserProfil> profils = apiService.handleResponse(apiResponse);

        return profils;
    }

    public UserProfil createUserProfil(UserProfil userProfil) {
        String url = csharpApiBaseUrl + "/api/UserProfil";
    
        ApiResponse<UserProfil> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.POST,
            userProfil,
            new ParameterizedTypeReference<ApiResponse<UserProfil>>() {}
        );
    
        return apiService.handleResponse(apiResponse);
    }
    

    public UserProfil updateUserProfil(int id, UserProfil userProfil) {
        String url = csharpApiBaseUrl + "/api/UserProfil/" + userProfil.getIdProfil();
        ApiResponse<UserProfil> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.PUT,
            userProfil,
            new ParameterizedTypeReference<ApiResponse<UserProfil>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }

    public Boolean deleteUserProfil(int id) {
        String url = csharpApiBaseUrl + "/api/UserProfil/" + id;

        ApiResponse<Boolean> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.DELETE,
            null,
            new ParameterizedTypeReference<ApiResponse<Boolean>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }
}