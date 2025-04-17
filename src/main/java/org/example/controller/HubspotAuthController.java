package org.example.controller;

import org.example.model.hubspot.HubspotAuthResponse;
import org.example.service.HubspotTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/hubspot")
public class HubspotAuthController {

    private static final String CLIENT_ID = "4583773f-97fb-4c6f-af55-3c61b1b28e66";
    private static final String REDIRECT_URI = "http://localhost:8080/hubspot/callback";
    private static final String SCOPE = "crm.objects.contacts.read crm.objects.contacts.write";
    private static final String AUTH_URL = "https://app.hubspot.com/oauth/authorize";

    private final HubspotTokenService tokenService;

    public HubspotAuthController(HubspotTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public Mono<ResponseEntity<Void>> login() {
        String url = UriComponentsBuilder
                .fromHttpUrl(AUTH_URL)
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("scope", SCOPE)
                .queryParam("response_type", "code")
                .toUriString();

        return Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build());
    }

    @GetMapping("/callback")
    public Mono<HubspotAuthResponse> callback(@RequestParam String code) {
        return tokenService.exchangeCodeForToken(code);
    }

}
