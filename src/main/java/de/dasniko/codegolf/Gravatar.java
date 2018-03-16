package de.dasniko.codegolf;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Component("Gravatar")
@RequiredArgsConstructor
public class Gravatar {

    private final AccessToken accessToken;

    public String imageUrl() throws NoSuchAlgorithmException {
        return imageUrlFrom(accessToken.getEmail());
    }

    public String imageUrlFrom(String email) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return "https://www.gravatar.com/avatar/" + hex(md.digest(email.getBytes())) + "?d=wavatar";
    }

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte anArray : array) {
            sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}
