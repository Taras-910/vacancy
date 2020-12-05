package ua.training.top.to;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class VacancyTo {

    @NotNull
    Integer vacancyId;

    @NotNull
    private String employerName;

    @NotNull
    private String address;

    @NotNull
    private String title;

    @NotNull
    private Date localDate;

    private Integer salaryMin;

    private Integer salaryMax;

    @NotNull
    String link;

    @NotNull
    String skills;

    private boolean toVote = false;

    public VacancyTo(@NotNull Integer vacancyId, @NotNull String employerName, @NotNull String address, @NotNull String title,
                     @NotNull Date localDate, Integer salaryMin, Integer salaryMax, String link, String skills, boolean toVote) {
        this.vacancyId = vacancyId;
        this.employerName = employerName;
        this.address = address;
        this.title = title;
        this.localDate = localDate;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.link = link;
        this.skills = skills;
        this.toVote = toVote;
    }

    public Integer getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(Integer vacancyId) {
        this.vacancyId = vacancyId;
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

    public boolean isToVote() {
        return toVote;
    }

    public void setToVote(boolean toVote) {
        this.toVote = toVote;
    }

    @Override
    public String toString() {
        return "\nVacancyTo{" +
                "\nvacancyId=" + vacancyId +
                ", \nemployerName='" + employerName + '\'' +
                ", \naddress='" + address + '\'' +
                ", \ntitle='" + title + '\'' +
                ", \nlocalDate=" + localDate +
                ", \nsalary=" + salaryMin +
                ", \nsalary=" + salaryMax +
                ", \nlink='" + link + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \ntoVote=" + toVote +
                '}';
    }
}
