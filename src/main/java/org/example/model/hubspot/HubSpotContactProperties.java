package org.example.model.hubspot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
