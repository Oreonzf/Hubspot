package org.example;

import org.example.model.hubspot.HubSpotTokenResponse;
import org.example.model.webhook.WebhookEvent;
import org.example.service.HubSpotOAuthService;
import org.example.service.WebhookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HubSpotServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private HubSpotOAuthService hubSpotOAuthService;

    @InjectMocks
    private WebhookService webhookService;

    @Test
    void testExchangeCodeForAccessToken() {
        String code = "test_code";

        HubSpotTokenResponse tokenResponse = new HubSpotTokenResponse(
                "access_token",
                "refresh_token",
                3600,
                "bearer",
                "contacts"
        );

        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HubSpotTokenResponse.class)).thenReturn(Mono.just(tokenResponse));

        String accessToken = hubSpotOAuthService.exchangeCodeForToken(code);

        assertNotNull(accessToken);
        assertEquals("access_token", accessToken);
    }

    @Test
    void testProcessWebhookEvent() {
        WebhookEvent event = new WebhookEvent(
                12345L,
                "email",
                "test@example.com",
                67890L,
                1L,
                123L,
                456L,
                System.currentTimeMillis(),
                "contact.creation",
                0
        );

        webhookService.processEvents(List.of(event));
    }

    @Test
    void testTokenExchangeFailure() {
        String code = "invalid_code";

        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(HubSpotTokenResponse.class))
                .thenReturn(Mono.error(new RuntimeException("Failed to exchange code for access token")));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            hubSpotOAuthService.exchangeCodeForToken(code);
        });

        assertEquals("Failed to obtain access token from HubSpot", exception.getMessage());
    }
}