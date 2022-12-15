package ua.training.top.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class RootController {
    public final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String root() {
        log.info("root");
        return "redirect:vacancies";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String getUsers() {
        log.info("getUsers");
        return "users";
    }

    @GetMapping(value = "/login")
    public String login() {
        log.info("login");
        return "login";
    }

    @GetMapping("/vacancies")
    public String getVacancies() {
        log.info("getVacancies");
        return "vacancies";
    }
}
