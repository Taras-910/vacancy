package ua.training.top.util;

import ua.training.top.model.Employer;
import ua.training.top.to.VacancyTo;

import java.util.List;

import static ua.training.top.util.MessageUtil.not_be_null;

public class EmployerUtil {

    public static List<Employer> getEmployersFromTos(List<VacancyTo> vTos) {
        return vTos.stream().map(EmployerUtil::getEmployerFromTo).toList();
    }

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        return new Employer(null, vTo.getEmployerName(), vTo.getAddress());
    }

    public static void checkDataEmployer(Employer e){
        String[] data = {e.getName(), e.getAddress()};
        for(String text : data) {
            if (text == null || text.equals("")) {
                throw new IllegalArgumentException("employer" + not_be_null);
            }
        }
    }
}
