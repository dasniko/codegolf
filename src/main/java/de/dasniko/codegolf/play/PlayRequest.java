package de.dasniko.codegolf.play;

import de.dasniko.codegolf.User;
import lombok.Data;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Data
public class PlayRequest {
    private User user;
    private String sourcecode = "return -1;";
}
