package ua.training.top.to;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class VacancyTo extends BaseTo{

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
    public VacancyTo(Integer id, @NotNull String title, @NotNull String employerName, @NotNull String address,
                     Integer salaryMin, Integer salaryMax, String link, String skills, @NotNull Date localDate, boolean toVote) {
        super(id);
        this.title = title;
        this.employerName = employerName;
        this.address = address;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.link = link;
        this.skills = skills;
        this.localDate = localDate;
        this.toVote = toVote;
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
                "\nid=" + id +
                ", \ntitle='" + title + '\'' +
                ", \nemployerName='" + employerName + '\'' +
                ", \naddress='" + address + '\'' +
                ", \nsalary=" + salaryMin +
                ", \nsalary=" + salaryMax +
                ", \nlink='" + link + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \nlocalDate=" + localDate +
                ", \ntoVote=" + toVote +
                '}';
    }
}
