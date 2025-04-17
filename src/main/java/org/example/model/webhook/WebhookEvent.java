package org.example.model.webhook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEvent {
    private Long objectId;
    private String propertyName;
    private String propertyValue;
    private Long eventId;
    private Long subscriptionId;
    private Long portalId;
    private Long appId;
    private Long occurredAt;
    private String subscriptionType;
    private int attemptNumber;
}
