package ua.training.top.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.model.Vacancy;
import ua.training.top.web.ui.VacancyController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static ua.training.top.util.DateTimeUtil.toDate;

public class VacancyServlet extends HttpServlet {
    private Logger log = LoggerFactory.getLogger(VacancyServlet.class);
    private VacancyController controller;
    private ClassPathXmlApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        controller = context.getBean(VacancyController.class);
    }

    @Override
    public void destroy() {
        context.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
/*
        Dish dish = new Dish(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            dishController.create(dish);
        } else {
            dishController.update(meal, getId(request));
        }
*/
        response.sendRedirect("vacancies");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        log.info("action {}", action);
        switch (action == null ? "all" : action) {
            case "delete":
                int employerId = Integer.parseInt(request.getParameter("employerId"));
                controller.delete(getId(request), employerId);
                response.sendRedirect("vacancies");
                break;
            case "create":
            case "update":
                final Vacancy vacancy = "create".equals(action) ?
                        new Vacancy("new Developer", toDate(2020, 10, 26), 500, "", "new knowledge") :
                        controller.get(getId(request), getId(request));
                request.setAttribute("vacancy", vacancy);
                request.getRequestDispatcher("/vacancyForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                List<Vacancy> list = controller.getAll();
                log.info("list {}", list);
                request.setAttribute("vacancies", list);
                request.getRequestDispatcher("/vacancies.jsp").forward(request, response);
                break;
        }
    }

    private int getId (HttpServletRequest request){
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
