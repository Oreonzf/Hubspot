package org.example.model.hubspot;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HubSpotContactProperties {
    private String firstname;
    private String lastname;
    private String email;
}
