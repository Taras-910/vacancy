package ua.training.top.to;

import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class VacancyTo extends BaseTo implements Serializable, Comparable<VacancyTo> {

    @NotNull
    @Size(min = 2, max = 250)
    private String title;

    @NotNull
    @Size(min = 2, max = 512)
    private String employerName;

    @NotNull
    @Size(min = 2, max = 512)
    private String address;

    @NotNull
    @Range(min = 1, max = 10000000)
    private Integer salaryMin;

    @NotNull
    @Range(min = 1, max = 10000000)
    private Integer salaryMax;

    @NotNull
    @Size(min = 4, max = 1000)
    String url;

    @NotNull
    @Size(min = 3, max = 1024)
    String skills;

    private LocalDate releaseDate;

    private String language;

    private String workplace;

    private boolean toVote = false;

    public VacancyTo(Integer id, @NotNull String title, @NotNull String employerName, @NotNull String address, @NotNull Integer salaryMin,
                     @NotNull Integer salaryMax, @NotNull String url, @NotNull String skills, @Nullable LocalDate releaseDate,
                     @Nullable String language, @Nullable String workplace, @Nullable boolean toVote) {
        super(id);
        this.title = title;
        this.employerName = employerName;
        this.address = address;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
        this.language = language;
        this.workplace = workplace;
        this.toVote = toVote;
    }

    public VacancyTo(){}

    public VacancyTo(VacancyTo v){
        this(v.getId(), v.getTitle(), v.getEmployerName(), v.getAddress(), v.getSalaryMin(), v.getSalaryMax(), v.getUrl(),
                v.getSkills(), v.getReleaseDate(), v.getLanguage(), v.getWorkplace(), v.isToVote());
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isToVote() {
        return toVote;
    }

    public void setToVote(boolean toVote) {
        this.toVote = toVote;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacancyTo vacancyTo = (VacancyTo) o;
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
        return "\n        VacancyTo{" +
                "\nid=" + id +
                ", \ntitle='" + title + '\'' +
                ", \nemployerName='" + employerName + '\'' +
                ", \naddress='" + address + '\'' +
                ", \nsalaryMin=" + salaryMin +
                ", \nsalaryMax=" + salaryMax +
                ", \nurl='" + url + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \nreleaseDate=" + releaseDate +
                ", \nlanguage=" + language +
                ", \nworkplace=" + workplace +
                ", \ntoVote=" + toVote +
                '}';
    }

    @Override
    public int compareTo(VacancyTo vTo) {
        String language = vTo.getLanguage().equals("all") ? "java" : vTo.getLanguage();
        return vTo.getTitle().toLowerCase().matches(".*\\b" + language + "\\b.*")
                && vTo.getTitle().toLowerCase().contains("middle") ? 1 : -1;
    }
}
