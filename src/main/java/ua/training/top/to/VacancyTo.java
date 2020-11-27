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

    private Integer salary;

    @NotNull
    String link;

    @NotNull
    String skills;

    private boolean toVote = false;

    public VacancyTo(@NotNull Integer vacancyId, @NotNull String employerName, @NotNull String address, @NotNull String title,
                     @NotNull Date localDate, Integer salary, String link, String skills, boolean toVote) {
        this.vacancyId = vacancyId;
        this.employerName = employerName;
        this.address = address;
        this.title = title;
        this.localDate = localDate;
        this.salary = salary;
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

    public boolean isToVote() {
        return toVote;
    }

    public void setToVote(boolean toVote) {
        this.toVote = toVote;
    }

    @Override
    public String toString() {
        return "VacancyTo{" +
                "vacancyId=" + vacancyId +
                ", employerName='" + employerName + '\'' +
                ", address='" + address + '\'' +
                ", title='" + title + '\'' +
                ", localDate=" + localDate +
                ", salary=" + salary +
                ", link='" + link + '\'' +
                ", skills='" + skills + '\'' +
                ", toVote=" + toVote +
                '}';
    }
}
