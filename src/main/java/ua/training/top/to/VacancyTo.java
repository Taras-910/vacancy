package ua.training.top.to;

import org.hibernate.validator.constraints.URL;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class VacancyTo extends BaseTo implements Serializable {

    @NotNull
    @Size(min = 2, max = 100)
    private String title;

    private String employerName;

    private String address;

    private Integer salaryMin;

    private Integer salaryMax;

    @URL
    String url;

    @NotNull
    @Size(min = 2, max = 1000)
    String skills;

    private Date releaseDate;

    private String siteName;

    private String language;

    private String workPlace;

    private boolean toVote = false;

    public VacancyTo(Integer id, @NotNull String title, @NotNull String employerName, @NotNull String address, Integer salaryMin, Integer salaryMax,
                     String url, String skills, @Nullable Date releaseDate, String siteName, String language, String workPlace, @Nullable boolean toVote) {
        super(id);
        this.title = title;
        this.employerName = employerName;
        this.address = address;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
        this.siteName = siteName;
        this.language = language;
        this.toVote = toVote;
        this.workPlace = workPlace;
    }

    public VacancyTo(){}

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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
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

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
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
                ", \nsiteName=" + siteName +
                ", \nlanguage=" + language +
                ", \nworkPlace=" + workPlace +
                ", \ntoVote=" + toVote +
                '}';
    }
}
