package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "vacancy", uniqueConstraints = {@UniqueConstraint(columnNames ={ "title", "skills"}, name = "vacancy_idx")})
public class Vacancy extends AbstractBaseEntity {

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @NotNull
    @Range(min = 1, max = 10000000)
    @Column(name = "salary_min")
    private Integer salaryMin;

    @NotNull
    @Range(min = 1, max = 10000000)
    @Column(name = "salary_max")
    private Integer salaryMax;

    @Column(name = "link")
    private String url;

    @NotNull
    @Column(name = "skills")
    private String skills;

    @Column(name = "release_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "employer_id", nullable = false)
    @JsonBackReference(value="employer-movement")  //https://stackoverflow.com/questions/20119142/jackson-multiple-back-reference-properties-with-name-defaultreference
    private Employer employer;

    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "freshen_id", nullable = false)
    @JsonBackReference(value = "freshen-movement") //https://stackoverflow.com/questions/20119142/jackson-multiple-back-reference-properties-with-name-defaultreference
    private Freshen freshen;

    public Vacancy() {
    }

    public Vacancy(String title, Integer salaryMin, Integer salaryMax, String url, String skills, LocalDate releaseDate) {
        this(null, title, salaryMin, salaryMax, url, skills, releaseDate);
    }

    public Vacancy(Integer id, String title, Integer salaryMin, Integer salaryMax, String url, String skills, LocalDate releaseDate) {
        super(id);
        this.title = title;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
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

    public Vacancy(Vacancy v) {
        this(v.getId(), v.getTitle(), v.getSalaryMin(), v.getSalaryMax(), v.getUrl(), v.getSkills(), v.getReleaseDate());
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

    public void setUrl(String url) {
        this.url = url;
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

    public Freshen getFreshen() {
        return freshen;
    }

    public void setFreshen(Freshen freshen) {
        this.freshen = freshen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacancy vacancy = (Vacancy) o;
        return  Objects.equals(title.toLowerCase(), vacancy.title.toLowerCase()) &&
                Objects.equals(skills.toLowerCase(), vacancy.skills.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title.toLowerCase(), skills.toLowerCase());
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
                ", \nid=" + id +
                ", \nemployer=" + employer +
                ", \nfreshen=" + freshen +
                '}';
    }
}
