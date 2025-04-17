package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubSpotContactService {

    private static final String HUBSPOT_API_URL = "https://api.hubapi.com";

    // Access token injected via @Value
    @Value("${hubspot.access-token}")
    private String accessToken;

    private final WebClient.Builder webClientBuilder;

    public Mono<String> createContact(String accessToken, String email, String firstName, String lastName) {
        WebClient webClient = WebClient.builder()
                .baseUrl(HUBSPOT_API_URL)
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .build();

        String contactJson = "{\n" +
                "  \"properties\": {\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"firstname\": \"" + firstName + "\",\n" +
                "    \"lastname\": \"" + lastName + "\"\n" +
                "  }\n" +
                "}";

        return webClient.post()
                .uri("/crm/v3/objects/contacts")
                .header("Content-Type", "application/json")
                .bodyValue(contactJson)
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(() -> System.out.println("Contact creation completed"));
    }
}
