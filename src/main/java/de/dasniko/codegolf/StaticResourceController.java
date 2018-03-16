package de.dasniko.codegolf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Controller
@RequestMapping("/")
public class StaticResourceController {

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }

    @RequestMapping(value = "rules", method = RequestMethod.GET)
    public String rules() {
        return "rules";
    }

    @RequestMapping(value = "awards", method = RequestMethod.GET)
    public String awards() {
        return "awards";
    }

}
