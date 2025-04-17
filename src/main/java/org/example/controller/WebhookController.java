package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.webhook.WebhookEvent;
import org.example.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/hubspot")
    public ResponseEntity<Void> receiveWebhook(@RequestBody List<WebhookEvent> events) {
        webhookService.processEvents(events);
        return ResponseEntity.ok().build();
    }
}
