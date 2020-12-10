package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    private String title;

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

    @Column(name="language")
    private String language;

    @Column(name="workplace")
    private String workplace;

    @Column(name="recorded_date")
    private LocalDateTime recordedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employer_id", nullable = false)
    @JsonBackReference
    private Employer employer;

    public Vacancy() {
    }

    public Vacancy(String title, Integer salaryMin, Integer salaryMax, String link, String skills, Date localDate, String language, String workplace, LocalDateTime recordedDate) {
        this(null, title, salaryMin, salaryMax, link, skills, localDate, language, workplace, recordedDate);
    }

    public Vacancy(Integer id, String title, Integer salaryMin, Integer salaryMax, String link, String skills, Date localDate, String language, String workplace, LocalDateTime recordedDate) {
        super(id);
        this.title = title;

        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.link = link;
        this.skills = skills;
        this.localDate = localDate;

        this.language = language;
        this.workplace = workplace;
        this.recordedDate = recordedDate;
    }

    public Vacancy(Integer id, String title, Integer salaryMin, Integer salaryMax, String link, String skills, Date localDate, Employer employer) {
        super(id);
        this.title = title;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.link = link;
        this.skills = skills;
        this.localDate = localDate;
    }

    public Vacancy(String title, Integer salaryMin, Integer salaryMax, String link, String skills, Date localDate) {
        this.title = title;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.link = link;
        this.skills = skills;
        this.localDate = localDate;
    }

    public Vacancy(Vacancy v) {
        this(v.getId(), v.getTitle(), v.getSalaryMin(), v.getSalaryMax(), v.getLink(), v.getSkills(), v.getLocalDate(), v.getLanguage(), v.getWorkplace(), v.getRecordedDate());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public LocalDateTime getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(LocalDateTime recordedDate) {
        this.recordedDate = recordedDate;
    }

    @Override
    public String toString() {
        return "\nVacancy{" +
                "\ntitle='" + title + '\'' +
                ", \nsalaryMin=" + salaryMin +
                ", \nsalaryMax=" + salaryMax +
                ", \nlink='" + link + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \nlocalDate=" + localDate +
                ", \nlanguage='" + language + '\'' +
                ", \nworkplace='" + workplace + '\'' +
                ", \nrecordedDate='" + recordedDate + '\'' +
                ", \nemployer=" + employer +
                ", \nid=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        Vacancy vacancy = (Vacancy) o;

        if (!getTitle().equals(vacancy.getTitle())) return false;
        return getLink().equals(vacancy.getLink());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getLink().hashCode();
        return result;
    }
}
