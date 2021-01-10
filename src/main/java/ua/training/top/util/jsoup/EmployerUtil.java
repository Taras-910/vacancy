package ua.training.top.util.jsoup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Employer;
import ua.training.top.to.VacancyTo;

import static ua.training.top.util.jsoup.datas.CorrectSiteName.getSiteName;

public class EmployerUtil {
    private static Logger log = LoggerFactory.getLogger(EmployerUtil.class);

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        return new Employer(null, vTo.getEmployerName(), vTo.getAddress(),
                vTo.getSiteName() == null ? getSiteName(vTo.getUrl()) : vTo.getSiteName());
    }
}
