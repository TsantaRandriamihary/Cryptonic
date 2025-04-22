package itu.p16.crypto.service.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import itu.p16.crypto.model.response.ApiResponse;
import itu.p16.crypto.model.user.Utilisateur;
import jakarta.servlet.http.HttpSession;

@Service
public class SignUpService {

    private final ApiService apiService;

    @Value("${csharp.api.base-url}")
    private String csharpApiBaseUrl;

    public SignUpService(ApiService apiService) {
        this.apiService = apiService;
    }

    public String sendSignUpRequest(String email, String password, HttpSession session) {
        String url = csharpApiBaseUrl + "/api/SignUp/signup";
        // Construire les données pour la requête
        SignUpRequest signUpRequest = new SignUpRequest(email, password);
        session.setAttribute("email", email);
        session.setAttribute("password", password);
        ApiResponse<String> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.POST, 
            signUpRequest, 
            new ParameterizedTypeReference<ApiResponse<String>>() {}
        );
        return apiService.handleResponse(apiResponse);
    }

    public Utilisateur confirmPin(String email, String password, String pin, HttpSession session) {
        String url = csharpApiBaseUrl + "/api/SignUp/pin-confirmation";
        PinConfirmationRequest pinRequest = new PinConfirmationRequest(pin, email, password);
        session.setAttribute("email", email);
        session.setAttribute("password", password);

        ApiResponse<Utilisateur> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.POST, 
            pinRequest, 
            new ParameterizedTypeReference<ApiResponse<Utilisateur>>() {}
        );
        return apiService.handleResponse(apiResponse);
    }
}

// DTOs (Data Transfer Objects)
class SignUpRequest {
    private String email;
    private String password;

    public SignUpRequest(String email, String password) {
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

class PinConfirmationRequest {
    private String pin;
    private String email;
    private String password;

    public PinConfirmationRequest(String pin, String email, String password) {
        this.pin = pin;
        this.email = email;
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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
