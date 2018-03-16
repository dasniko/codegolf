package de.dasniko.codegolf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.AccessToken;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public String getPackagename() {
        return null != username ? username.replaceAll("\\W", "") : "";
    }

    public static User from(AccessToken accessToken) {
        return User.builder()
                .username(accessToken.getPreferredUsername())
                .email(accessToken.getEmail())
                .firstName(accessToken.getGivenName())
                .lastName(accessToken.getFamilyName())
                .build();
    }
}
