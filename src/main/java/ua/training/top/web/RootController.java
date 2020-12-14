package ua.training.top.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
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
}

/*
@Controller
public class RootController {
    @Autowired
    private UserService userService;
    @Autowired
    private VacancyController vacancyController;


    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

*/
/*
    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }
*//*


    @PostMapping("/users")
    public String setUser(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("userId"));
        SecurityUtil.setAuthUserId(userId);
        return "redirect:vacancies";
    }

    @GetMapping("/vacancies")
    public String getVacancies(Model model) {
        model.addAttribute("vacancies", vacancyController.getAllTos());
        return "vacancies";
    }
}
*/
