package org.example.controller;

import org.example.service.HubSpotOAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hubspot")
public class OAuthController {

    private final HubSpotOAuthService oAuthService;

    public OAuthController(HubSpotOAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/oauth/callback")
    public ResponseEntity<String> oauthCallback(@RequestParam("code") String code) {
        String accessToken = oAuthService.exchangeCodeForToken(code);
        return ResponseEntity.ok("Access token: " + accessToken);
    }
}