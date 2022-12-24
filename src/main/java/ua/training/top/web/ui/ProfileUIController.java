package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.SecurityUtil;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.service.UserService;
import ua.training.top.web.AbstractUserController;

import javax.validation.Valid;

@ApiIgnore
@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {
public static final Logger log = LoggerFactory.getLogger(ProfileUIController.class);
    @Autowired
    UserService service;

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid @ModelAttribute User user, BindingResult result, SessionStatus status, ModelMap model) {
        log.info("updateProfile user {}", user);
        if (result.hasErrors()) {
            model.addAttribute("register", false);
            return "profile";
        }
        else {
            service.update(user, SecurityUtil.authUserId());
            SecurityUtil.get().update(user);
            status.setComplete();
            return "redirect:/vacancies";
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        log.info("register");
        model.addAttribute("user", new User(null, "", "", "", Role.USER));
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid @ModelAttribute User user, BindingResult result, SessionStatus status, ModelMap model) {
        log.info("saveRegister {}", user);
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        }
        else {
            service.create(user);
            status.setComplete();
            return "redirect:/login?message=user.invite&username=" + user.getEmail();
        }
    }

}


