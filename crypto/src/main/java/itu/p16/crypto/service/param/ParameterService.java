package itu.p16.crypto.service.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import itu.p16.crypto.model.param.Parametre;
import itu.p16.crypto.model.response.ApiResponse;
import itu.p16.crypto.service.connection.ApiService;


@Service("paramService")
public class ParameterService {

    private final ApiService apiService;

    @Value("${csharp.api.base-url}")
    private String csharpApiBaseUrl;

    public ParameterService(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<Parametre> getAllParametres() {
        String url = csharpApiBaseUrl + "/api/Parametre";
        ApiResponse<List<Parametre>> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<List<Parametre>>>() {}
        );
        
        return apiService.handleResponse(apiResponse);
    }

    public Parametre getParametreById(int id) {
        String url = csharpApiBaseUrl + "/api/Parametre/" + id;
        ApiResponse<Parametre> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<Parametre>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }

    public Parametre createParametre(Parametre Parametre) {
        String url = csharpApiBaseUrl + "/api/Parametre";

        ApiResponse<Parametre> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.POST,
            Parametre,
            new ParameterizedTypeReference<ApiResponse<Parametre>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }

    public Parametre updateParametre(int id, Parametre parametre) {
        String url = csharpApiBaseUrl + "/api/Parametre/" + id;
        ApiResponse<Parametre> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.PUT,
            parametre,
            new ParameterizedTypeReference<ApiResponse<Parametre>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }

    public Boolean deleteParametre(int id) {
        String url = csharpApiBaseUrl + "/api/Parametre/" + id;

        ApiResponse<Boolean> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.DELETE,
            null,
            new ParameterizedTypeReference<ApiResponse<Boolean>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }

}
