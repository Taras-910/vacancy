package ua.training.top.web.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ua.training.top.SecurityUtil;
import ua.training.top.model.User;
import ua.training.top.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileUIController {

    @Autowired
    UserService service;

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid User user, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "profile";
        } else {
            service.update(user, SecurityUtil.authUserId());
            SecurityUtil.get().update(user);
            status.setComplete();
            return "redirect:vacancies";
        }
    }
}
