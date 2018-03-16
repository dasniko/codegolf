package de.dasniko.codegolf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Niko Köbler, https://www.n-k.de, @dasniko
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "codegolf")
public class CodegolfConfig {
    private String lambdaUrl;
    private boolean showResults;
    private boolean submissionsAllowed;
    private String admin;
}
