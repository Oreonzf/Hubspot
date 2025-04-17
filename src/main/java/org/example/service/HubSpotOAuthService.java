package org.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // CERTO!!!
import lombok.extern.slf4j.Slf4j;
import org.example.model.hubspot.HubSpotTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubSpotOAuthService {

    @Value("${hubspot.client-id}")
    private String clientId;

    @Value("${hubspot.client-secret}")
    private String clientSecret;

    @Value("${hubspot.redirect-uri}")
    private String redirectUri;

    private final WebClient.Builder webClientBuilder;

    public String generateAuthorizationUrl() {
        String scope = "contacts";
        String url = "https://app.hubspot.com/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope=" + scope +
                "&response_type=code";

        return url;
    }

    public String exchangeCodeForToken(String code) {
        WebClient webClient = webClientBuilder.build();

        try {
            HubSpotTokenResponse response = webClient.post()
                    .uri("https://api.hubapi.com/oauth/v1/token")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .bodyValue("grant_type=authorization_code" +
                            "&client_id=" + clientId +
                            "&client_secret=" + clientSecret +
                            "&redirect_uri=" + redirectUri +
                            "&code=" + code)
                    .retrieve()
                    .bodyToMono(HubSpotTokenResponse.class)
                    .block();

            if (response != null && response.getAccessToken() != null) {
                // Here, you can save the token or use it in your application
                return response.getAccessToken();
            } else {
                throw new RuntimeException("Received empty response or missing access token");
            }
        } catch (Exception e) {
            log.error("Failed to exchange code for access token", e);
            throw new RuntimeException("Failed to obtain access token from HubSpot", e);
        }
    }
}