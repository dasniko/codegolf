package de.dasniko.codegolf.results;

import de.dasniko.codegolf.CodegolfConfig;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessToken;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Controller
@RequestMapping("results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;
    private final AccessToken accessToken;
    private final MessageSource messageSource;
    private final CodegolfConfig config;

    @RequestMapping(method = RequestMethod.GET)
    public String getResults(Model model) {
        List<ResultEntry> entries = resultService.getResultlist();
        model.addAttribute(entries);
        return "results";
    }

    @ResponseBody
    @RequestMapping(path = "sourcecode", method = RequestMethod.GET)
    public String getSourcecode(@RequestParam String username) {
        try {
            if (config.isShowResults() || (null != accessToken && accessToken.getPreferredUsername().equals(config.getAdmin()))) {
                return resultService.getResultEntry(username).getSourceCode();
            } else {
                if (null != accessToken && username.equalsIgnoreCase(accessToken.getPreferredUsername())) {
                    return resultService.getResultEntry(username).getSourceCode();
                } else {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            String message = messageSource.getMessage("results.forbidden", null, LocaleContextHolder.getLocale());
            throw new NotAllowedException(message);
        }
    }

}
