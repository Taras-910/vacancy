package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.FetchType.EAGER;

@NamedQueries({
        @NamedQuery(name = Vacancy.DELETE, query = "DELETE FROM Vacancy v WHERE v.id=:id AND v.employer.id=:employerId"),
        @NamedQuery(name = Vacancy.ALL_SORTED, query = "SELECT v FROM Vacancy v ORDER BY v.releaseDate DESC"),
})
@Entity
@Table(name = "vacancy", uniqueConstraints = {@UniqueConstraint(columnNames = "skills", name = "vacancies_skills_idx")})
public class Vacancy extends AbstractBaseEntity {

    public static final String DELETE = "Vacancy.delete";
    public static final String ALL_SORTED = "Vacancy.getAll";

    @Column(name = "title", nullable = false, unique = true)
    @NotNull
    private String title;

    @Column(name="salary_min")
    @Range(max = 10000000)
    private Integer salaryMin;

    @Column(name="salary_max")
    @Range(max = 10000000)
    private Integer salaryMax;

    @Column(name="link")
    private String url;

    @Column(name="skills")
    private String skills;

    @Column(name="local_date", nullable = false)
    private Date releaseDate;

    @Column(name="language")
    private String language;

    @Column(name="workplace")
    private String workplace;

    @Column(name="recorded_date")
    private LocalDateTime recordedDate;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "employer_id", nullable = false)
    @JsonBackReference
    private Employer employer;

    public Vacancy() {
    }

    public Vacancy(String title, Integer salaryMin, Integer salaryMax, String url, String skills, Date releaseDate, String language, String workplace, LocalDateTime recordedDate) {
        this(null, title, salaryMin, salaryMax, url, skills, releaseDate, language, workplace, recordedDate);
    }

    public Vacancy(Integer id, String title, Integer salaryMin, Integer salaryMax, String url, String skills, Date releaseDate, String language, String workplace, LocalDateTime recordedDate) {
        super(id);
        this.title = title;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
        this.language = language;
        this.workplace = workplace;
        this.recordedDate = recordedDate;
    }

    public Vacancy(Integer id, String title, Integer salaryMin, Integer salaryMax, String url, String skills, Date releaseDate, Employer employer) {
        super(id);
        this.title = title;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
    }

    public Vacancy(String title, Integer salaryMin, Integer salaryMax, String url, String skills, Date releaseDate) {
        this.title = title;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
    }

    public Vacancy(Vacancy v) {
        this(v.getId(), v.getTitle(), v.getSalaryMin(), v.getSalaryMax(), v.getUrl(), v.getSkills(), v.getReleaseDate(), v.getLanguage(), v.getWorkplace(), v.getRecordedDate());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date localDate) {
        this.releaseDate = localDate;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String link) {
        this.url = link;
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
                ", \nurl='" + url + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \nreleaseDate=" + releaseDate +
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
        return getUrl().equals(vacancy.getUrl());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getUrl().hashCode();
        return result;
    }
}
