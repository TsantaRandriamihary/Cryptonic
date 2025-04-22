package itu.p16.crypto.service.genre;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import itu.p16.crypto.service.connection.ApiService;
import itu.p16.crypto.model.genre.Genre;
import itu.p16.crypto.model.response.ApiResponse;

import java.util.List;

@Service
public class GenreService {

    private final ApiService apiService;

    @Value("${csharp.api.base-url}")
    private String csharpApiBaseUrl;

    public GenreService(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<Genre> getAllGenre() {
        String url = csharpApiBaseUrl + "/api/Genre";
        ApiResponse<List<Genre>> apiResponse = apiService.sendRequest(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<List<Genre>>>() {}
        );

        return apiService.handleResponse(apiResponse);
    }
}