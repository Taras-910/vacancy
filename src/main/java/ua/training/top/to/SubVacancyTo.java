package ua.training.top.to;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static ua.training.top.util.xss.XssUtil.xssClear;

public class SubVacancyTo {
    private String title;
    private String employerName;
    private String skills;

    public SubVacancyTo(@NotNull String title, @NotNull String employerName, @NotNull String skills) {
        this.title = xssClear(title);
        this.employerName = xssClear(employerName);
        this.skills = xssClear(skills);
    }

    public SubVacancyTo(VacancyTo v) {
        this(v.getTitle(), v.getEmployerName(), v.getSkills());
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubVacancyTo vacancyTo = (SubVacancyTo) o;
        return  Objects.equals(title, vacancyTo.title) &&
                Objects.equals(employerName, vacancyTo.employerName) &&
                Objects.equals(skills, vacancyTo.skills);
    }
    @Override
    public int hashCode() {
        return Objects.hash(title, employerName, skills);
    }
}
