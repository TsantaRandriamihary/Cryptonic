package itu.p16.crypto.service.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import itu.p16.crypto.model.response.ApiResponse;
import itu.p16.crypto.model.user.Utilisateur;

@Service
public class LoginService {

    private final ApiService apiService;

    @Value("${csharp.api.base-url}")
    private String csharpApiBaseUrl;

    public LoginService(ApiService apiService) {
        this.apiService = apiService;
    }

    public Utilisateur loginUser(String email, String password) {
        String url = csharpApiBaseUrl + "/api/Login/login";
        LoginRequest loginRequest = new LoginRequest(email, password);

        ApiResponse<Utilisateur> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.POST,
            loginRequest,
            new ParameterizedTypeReference<ApiResponse<Utilisateur>>() {}
        );
        
        return apiService.handleResponse(apiResponse);
    }

    public Utilisateur validatePin(String email, String pin) {
        String url = csharpApiBaseUrl + "/api/Login/validate-pin";
        PinValidationRequest pinRequest = new PinValidationRequest(email, pin);

        ApiResponse<Utilisateur> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.POST,
            pinRequest,
            new ParameterizedTypeReference<ApiResponse<Utilisateur>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }
}


class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class PinValidationRequest {
    private String email;
    private String pin;

    public PinValidationRequest(String email, String pin) {
        this.email = email;
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
