package de.dasniko.codegolf.results;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String message) {
        super(message);
    }
}
