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
@Table(name = "vacancy", uniqueConstraints = {@UniqueConstraint(columnNames = "skills", name = "vacancies_title_skills_idx")})
public class Vacancy extends AbstractBaseEntity {

    public static final String DELETE = "Vacancy.delete";
    public static final String ALL_SORTED = "Vacancy.getAll";

    @Column(name = "title", nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(name="local_date", nullable = false)
    @NotNull
    private Date localDate;

    @Column(name="salary_min")
    @Range(max = 10000000)
    private Integer salaryMin;

    @Column(name="salary_max")
    @Range(max = 10000000)
    private Integer salaryMax;

    @Column(name="link")
    private String link;

    @Column(name="skills")
    private String skills;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employer_id", nullable = false)
    @JsonBackReference
    private Employer employer;

    public Vacancy() {
    }

    public Vacancy(String name, Date localDate, Integer salaryMin, Integer salaryMax, String link, String skills) {
        this(null, name, localDate, salaryMin, salaryMax, link, skills);
    }

    public Vacancy(Integer id, String name, Date localDate, Integer salaryMin, Integer salaryMax, String link, String skills) {
        super(id);
        this.name = name;
        this.localDate = localDate;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.link = link;
        this.skills = skills;
    }

    public Vacancy(Integer id, String name, Date localDate, Integer salaryMin, Integer salaryMax, String link, String skills, Employer employer) {
        super(id);
        this.name = name;
        this.localDate = localDate;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.link = link;
        this.skills = skills;
    }

    public Vacancy(Vacancy v) {
        this(v.getId(), v.getName(), v.getLocalDate(), v.getSalaryMin(), v.getSalaryMax(), v.getLink(), v.getSkills());
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

    public Integer getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Integer salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Integer getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Integer salaryMax) {
        this.salaryMax = salaryMax;
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
                ", \nsalaryMin=" + salaryMin +
                ", \nsalaryMax=" + salaryMax +
                ", \nlink='" + link + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \nemployer=" + employer +
                ", \nid=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        Vacancy vacancy = (Vacancy) o;

        if (!getName().equals(vacancy.getName())) return false;
        return getLink().equals(vacancy.getLink());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getLink().hashCode();
        return result;
    }
}
