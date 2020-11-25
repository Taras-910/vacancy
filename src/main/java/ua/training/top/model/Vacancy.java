package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = Vacancy.DELETE, query = "DELETE FROM Vacancy v WHERE v.id=:id AND v.employer.id=:employerId"),
        @NamedQuery(name = Vacancy.ALL_SORTED, query = "SELECT v FROM Vacancy v ORDER BY v.localDate DESC"),
})
@Entity
@Table(name = "vacancy", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"local_date", "title", "employer_id"}, name = "vacancies_date_employer_idx")})
public class Vacancy extends AbstractBaseEntity {

    public static final String DELETE = "Vacancy.delete";
    public static final String ALL_SORTED = "Vacancy.getAll";

    @Column(name = "title", nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(name="local_date", nullable = false)
    @NotNull
    private Date localDate;

    @Column(name="salary")
    @Range(max = 10000000)
    private Integer salary;

    @Column(name="link")
    String link;

    @Column(name="skills")
    String skills;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employer_id", nullable = false)
    @JsonBackReference
    private Employer employer;

    public Vacancy() {
    }

    public Vacancy(String name, Date localDate, Integer salary, String link, String skills) {
        this(null, name, localDate, salary, link, skills);
    }

    public Vacancy(Integer id, String name, Date localDate, Integer salary, String link, String skills) {
        super(id);
        this.name = name;
        this.localDate = localDate;
        this.salary = salary;
        this.link = link;
        this.skills = skills;
    }

    public Vacancy(Vacancy v) {
        this(v.getId(), v.getName(), v.getLocalDate(), v.getSalary(), v.getLink(), v.getSkills());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLocalDate() {
        return localDate;
    }

    public void setLocalDate(Date localDate) {
        this.localDate = localDate;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    @Override
    public String toString() {
        return "\nVacancy{" +
                "\nname='" + name + '\'' +
                ", \nlocalDate=" + localDate +
                ", \nsalary=" + salary +
                ", \nlink='" + link + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \nemployer=" + employer +
                ", \nid=" + id +
                '}';
    }
}
