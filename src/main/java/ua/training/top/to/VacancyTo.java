package ua.training.top.to;

import ua.training.top.model.Vacancy;

import javax.validation.constraints.NotNull;
import java.util.List;

public class VacancyTo {

    @NotNull
    Integer employerId;

    @NotNull
    private String employerName;

    @NotNull
    private List<Vacancy> vacancies;

    private boolean toVote = false;

    private List<String> searchWords;

    private List<String> excludeWords;

    public VacancyTo(@NotNull int employerId, @NotNull String employerName, @NotNull List<Vacancy> vacancies, boolean toVote) {
        this.employerId = employerId;
        this.employerName = employerName;
        this.vacancies = vacancies;
        this.toVote = toVote;
    }

    public VacancyTo(VacancyTo menu){
        this(menu.getEmployerId() ,menu.getEmployerName() ,menu.getVacancies() ,menu.isToVote());
    }

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public boolean isToVote() {
        return toVote;
    }

    public void setToVote(boolean toLike) {
        this.toVote = toLike;
    }

    @Override
    public String toString() {
        return "VacancyTo{" +
                "employerId=" + employerId +
                ", employerName='" + employerName + '\'' +
                ", vacancies=" + vacancies +
                ", toVote=" + toVote +
                '}';
    }
}
