package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ua.training.top.SecurityUtil;
import ua.training.top.model.User;
import ua.training.top.service.UserService;

import javax.validation.Valid;
//•	Поменял путь регистрации с /registered на /profile/registered (в ProfileUIController вместо RootController)
@Controller
@RequestMapping("/profile")
public class ProfileUIController {
public static final Logger log = LoggerFactory.getLogger(ProfileUIController.class);
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

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid User user, BindingResult result, SessionStatus status, ModelMap model) {
        log.info("saveRegister user={} result={} status={}, model={}", user, result, status, model);
        if (result.hasErrors()) {
            log.info("hasErrors");
            model.addAttribute("register", true);
            return "profile";
        } else {
            try {
                service.create(user);
                status.setComplete();
                return "redirect:/login?message=Вы зарегистрированы. Введите ваш логин/пароль&username=" + user.getEmail();
            } catch (Exception e) {
            throw new DataIntegrityViolationException("user " + user + " уже существует в базе данных");
            }
        }
    }
}

