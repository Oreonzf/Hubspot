package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.hubspot.HubSpotWebhookEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hubspot")
@Slf4j
public class HubSpotWebhookController {

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveWebhook(@RequestBody List<HubSpotWebhookEvent> events) {
        for (HubSpotWebhookEvent event : events) {
            if ("contact.creation".equals(event.getSubscriptionType())) {
                log.info("Novo contato criado no HubSpot! ID: {}", event.getObjectId());
            } else {
                log.info("Evento recebido: {}", event.getSubscriptionType());
            }
        }
        return ResponseEntity.ok().build();
    }
}