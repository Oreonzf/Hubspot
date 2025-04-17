package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.request.ContactRequest;
import org.example.service.HubSpotContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hubspot")
@RequiredArgsConstructor
public class ContactController {

    private final HubSpotContactService contactService;

    @PostMapping("/contact")
    public ResponseEntity<String> createContact(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody ContactRequest contactRequest
    ) {
        if (!authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header format.");
        }

        String accessToken = authorization.substring(7); // Remove "Bearer "
        contactService.createContact(
                accessToken,
                contactRequest.getEmail(),
                contactRequest.getFirstName(),
                contactRequest.getLastName()
        );
        return ResponseEntity.ok("Contact created successfully.");
    }
}
