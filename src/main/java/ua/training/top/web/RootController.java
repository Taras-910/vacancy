package ua.training.top.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.training.top.service.VacancyService;


@Controller
public class RootController {

    @Autowired
    private VacancyService vacancyService;

    @GetMapping("/")
    public String root() {
        return "redirect:vacancies";
    }

    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping("/vacancies")
    public String getMeals(Model model) {
        model.addAttribute("vacancies", vacancyService.getAll());
        return "vacancies";
    }
}


/*@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:vacancies";
    }

    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping("/vacancies")
    public String getVacancies() {
        return "vacancies";
    }
}*/
