package org.example.model.hubspot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HubSpotCreateContactRequest {
    private HubSpotContactProperties properties;
}
