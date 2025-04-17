package org.example.model.hubspot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSpotWebhookEvent {
    private String subscriptionType;
    private Long eventId;
    private Long objectId;
    private String propertyName;
    private String propertyValue;
    private String changeSource;
    private String eventType;
    private Long timestamp;
    private Long appId;
    private Long portalId;
}
