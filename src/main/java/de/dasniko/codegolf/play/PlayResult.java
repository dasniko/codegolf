package de.dasniko.codegolf.play;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayResult {
    private boolean success;
    private String result;
    private int count;

    String getResultString() {
        return result + " " + count;
    }
}
