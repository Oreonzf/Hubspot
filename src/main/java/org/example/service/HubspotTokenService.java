package org.example.service;

import org.example.model.hubspot.HubspotAuthResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class HubspotTokenService {

    private static final String TOKEN_URL = "https://api.hubapi.com/oauth/v1/token";

    public Mono<HubspotAuthResponse> exchangeCodeForToken(String code) {
        return WebClient.create()
                .post()
                .uri(TOKEN_URL)
                .headers(headers -> {
                    headers.setBasicAuth("4583773f-97fb-4c6f-af55-3c61b1b28e66", "defd3b51-a565-4c33-9b3b-e1b41b556e0e");
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", "4583773f-97fb-4c6f-af55-3c61b1b28e66")
                        .with("client_secret", "defd3b51-a565-4c33-9b3b-e1b41b556e0e")
                        .with("redirect_uri", "http://localhost:8080/hubspot/callback")
                        .with("code", code))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    return Mono.error(new RuntimeException("HubSpot Auth failed with status: " + response.statusCode()));
                })
                .onStatus(status -> status.is5xxServerError(), response -> {
                    return Mono.error(new RuntimeException("HubSpot Auth failed with server error"));
                })
                .bodyToMono(HubspotAuthResponse.class);
    }
}
