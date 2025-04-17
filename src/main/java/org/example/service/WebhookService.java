package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.model.webhook.WebhookEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WebhookService {

    public void processEvents(List<WebhookEvent> events) {
        for (WebhookEvent event : events) {
            log.info("Webhook recebido: {}", event);
            // Aqui você pode tratar os dados, salvar em banco, etc.
        }
    }
}