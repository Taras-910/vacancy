package ua.training.top.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;
import ua.training.top.web.jsp.VacancyController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
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
        log.info("doPost toVote");
        String action = request.getParameter("action");
        log.info("action {}", action);
        String id = request.getParameter("action");
        log.info("id {}", id);

        int vacancyId = 0;
        if(id != null)  vacancyId = Integer.parseInt(id);
        boolean toVote  =  true;
        // extract other information to set the obj, username, accessType, and allowDeny variables

        log.info("vacancyId {}", vacancyId);
        log.info("toVote {}", toVote);

        controller.enable(100055, toVote);

        response.sendRedirect("vacancies");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.info("doGet toVote");
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
                        new Vacancy("new Developer", 100, 500, "",
                                "new knowledge", toDate(2020, 10, 26), "newLanguage", "newResidence", LocalDateTime.now()) :
                        controller.get(getId(request), getId(request));
                request.setAttribute("vacancy", vacancy);
                request.getRequestDispatcher("/vacancyForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                List<VacancyTo> list = controller.getAllTos();
//                log.info("list {}", list);
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
