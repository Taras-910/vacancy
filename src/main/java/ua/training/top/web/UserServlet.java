package ua.training.top.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.SecurityUtil;
import ua.training.top.aggregator.AggregatorController;
import ua.training.top.web.ui.UserController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class UserServlet extends HttpServlet {
    private Logger log = LoggerFactory.getLogger(UserServlet.class);
    private UserController userController;
    private ClassPathXmlApplicationContext context;
    private AggregatorController aggController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        userController = context.getBean(UserController.class);
        aggController = context.getBean(AggregatorController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        log.info("userId {}", userId);
        SecurityUtil.setAuthUserId(userId);

        response.sendRedirect("vacancies");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("getAll");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        log.info("action {}", action);
        if(action.equals("refresh")) {
            aggController.refreshDB("за_рубежем", "java");
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("users", userController.getAll());
        request.getRequestDispatcher("/users.jsp").forward(request, response);

    }
}
