package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;

@NamedQueries({
        @NamedQuery(name = Vacancy.DELETE, query = "DELETE FROM Vacancy v WHERE v.id=:id AND v.employer.id=:employerId"),
        @NamedQuery(name = Vacancy.ALL_SORTED, query = "SELECT v FROM Vacancy v ORDER BY v.releaseDate DESC"),
})
@Entity
@Table(name = "vacancy", uniqueConstraints = {@UniqueConstraint(columnNames ={ "title", "skills"}, name = "vacancy_idx")})
public class Vacancy extends AbstractBaseEntity {

    public static final String DELETE = "Vacancy.delete";
    public static final String ALL_SORTED = "Vacancy.getAll";

    @NotNull
//    @Size(min = 2, max = 256)
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @NotNull
//    @Range(min = 1, max = 10000000)
    @Column(name="salary_min")
    private Integer salaryMin;

    @NotNull
//    @Range(min = 1, max = 10000000)
    @Column(name="salary_max")
    private Integer salaryMax;

    @Column(name="link")
    private String url;

    @NotNull
//    @Size(min = 2, max = 256)
    @Column(name="skills")
    private String skills;

    @Column(name="release_date")
    private LocalDate releaseDate;

    @NotNull
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

    public Vacancy(String title, Integer salaryMin, Integer salaryMax, String url, String skills, LocalDate releaseDate, String language, String workplace, LocalDateTime recordedDate) {
        this(null, title, salaryMin, salaryMax, url, skills, releaseDate, language, workplace, recordedDate);
    }

    public Vacancy(Integer id, String title, Integer salaryMin, Integer salaryMax, String url, String skills, LocalDate releaseDate, String language, String workplace, LocalDateTime recordedDate) {
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

    public Vacancy(Integer id, String title, Integer salaryMin, Integer salaryMax, String url, String skills, LocalDate releaseDate, Employer employer) {
        super(id);
        this.title = title;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
    }

    public Vacancy(String title, Integer salaryMin, Integer salaryMax, String url, String skills, LocalDate releaseDate) {
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate localDate) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacancy vacancy = (Vacancy) o;
        return  Objects.equals(title, vacancy.title) &&
                Objects.equals(skills, vacancy.skills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, skills);
    }

    @Override
    public String toString() {
        return "\n\nVacancy{" +
                "\ntitle='" + title + '\'' +
                ", \nsalaryMin=" + salaryMin +
                ", \nsalaryMax=" + salaryMax +
                ", \nurl='" + url + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \nreleaseDate=" + releaseDate +
                ", \nlanguage='" + language + '\'' +
                ", \nworkplace='" + workplace + '\'' +
                ", \nrecordedDate='" + recordedDate + '\'' +
                ", \nid=" + id +
                ", \nemployer=" + employer +
                '}';
    }
}
