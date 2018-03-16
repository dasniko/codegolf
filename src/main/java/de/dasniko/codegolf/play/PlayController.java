package de.dasniko.codegolf.play;

import de.dasniko.codegolf.CodegolfConfig;
import de.dasniko.codegolf.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Slf4j
@Controller
@RequestMapping("play")
@RequiredArgsConstructor
public class PlayController {

    private final PlayService playService;
    private final AccessToken accessToken;
    private final CodegolfConfig config;

    @GetMapping
    public String start(Model model) {
        model.addAttribute(new PlayRequest());
        model.addAttribute("submissionAllowed", config.isSubmissionsAllowed());
        return "play";
    }

    @PostMapping
    public String play(PlayRequest playRequest, Model model) {
        if (config.isSubmissionsAllowed()) {
            User user = User.from(accessToken);
            log.info("Play Request from " + user.getUsername());
            playRequest.setUser(user);
            PlayResult result = playService.play(playRequest);
            log.info("Play Result for " + user.getUsername() + ": " + result.getResultString());
            model.addAttribute(playRequest);
            model.addAttribute(result);
        } else {
            model.addAttribute(new PlayRequest());
        }
        model.addAttribute("submissionAllowed", config.isSubmissionsAllowed());
        return "play";
    }

}
