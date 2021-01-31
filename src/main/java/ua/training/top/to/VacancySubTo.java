package ua.training.top.to;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

import static ua.training.top.util.xss.XssUtil.xssClear;

public class VacancySubTo extends BaseTo implements Serializable {

    private String title;
    private String employerName;
    private String skills;

    public VacancySubTo(Integer id, @NotNull String title, @NotNull String employerName, @NotNull String skills) {
        super(id);
        this.title = xssClear(title);
        this.employerName = xssClear(employerName);
        this.skills = xssClear(skills);
    }

    public VacancySubTo(){}

    public VacancySubTo(VacancyTo v){
        this(v.getId(), v.getTitle(), v.getEmployerName(), v.skills);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacancySubTo vacancyTo = (VacancySubTo) o;
        return  Objects.equals(title, vacancyTo.title) &&
                Objects.equals(employerName, vacancyTo.employerName) &&
                Objects.equals(skills, vacancyTo.skills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, employerName, skills);
    }

    @Override
    public String toString() {
        return "\n        VacancySubTo{" +
                "\nid=" + id +
                ", \ntitle='" + title + '\'' +
                ", \nemployerName='" + employerName + '\'' +
                ", \nskills='" + skills + '\'' +
                '}';
    }
}
