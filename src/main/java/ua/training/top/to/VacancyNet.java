package ua.training.top.to;

public class VacancyNet {

    private String title;
    private String date;
    private String salary;
    private String url;
    private String skills;
    private String city;
    private String siteName;
    private String companyName;
/*    public VacancyTo(Integer id, @NotNull String title, @NotNull String employerName, @NotNull String address, Integer salaryMin,
                     Integer salaryMax, String url, String skills, @NotNull Date releaseDate, String language, boolean toVote) {
        super(id);
        this.title = title;
        this.releaseDate = releaseDate;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.url = url;
        this.skills = skills;
         this.address = address;

        this.employerName = employerName;



        this.language = language;
        this.toVote = toVote;
*/
    public VacancyNet(String title, String date, String salary, String url, String skills, String city,
                      String siteName, String companyName) {
        this.title = title;
        this.date = date;
        this.salary = salary;
        this.url = url;
        this.skills = skills;
        this.city = city;
        this.siteName = siteName;
        this.companyName = companyName;
    }

    public VacancyNet() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VacancyNet)) return false;

        VacancyNet that = (VacancyNet) o;
        return getSkills().equalsIgnoreCase(that.getSkills());
    }

    @Override
    public int hashCode() {
        return getSkills().toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "VacancyNet{" +
                "\ntitle='" + title + '\'' +
                ", \ndate='" + date + '\'' +
                ", \nsalary='" + salary + '\'' +
                ", \nurl='" + url + '\'' +
                ", \nskills='" + skills + '\'' +
                ", \ncity='" + city + '\'' +
                ", \nsiteName='" + siteName + '\'' +
                ", \ncompanyName='" + companyName + '\'' +
                '}';
    }
}
