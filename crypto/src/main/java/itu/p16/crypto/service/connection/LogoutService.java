package itu.p16.crypto.service.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import itu.p16.crypto.model.response.ApiResponse;
import itu.p16.crypto.model.user.Utilisateur;
import jakarta.servlet.http.HttpSession;

@Service
public class LogoutService {

    private final ApiService apiService;

    @Value("${csharp.api.base-url}")
    private String csharpApiBaseUrl;

    public LogoutService(ApiService apiService) {
        this.apiService = apiService;
    }

    public String logout(HttpSession session) {
        String url = csharpApiBaseUrl + "/api/LogOut/logout";
        ApiResponse<String> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<String>>() {}
        );
        session.invalidate();
        return apiService.handleResponse(apiResponse);
    }
}