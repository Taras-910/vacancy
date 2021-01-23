package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "employer", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "address"}, name = "employers_idx")})
public class Employer extends AbstractBaseEntity{

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name="address")
    private String address;

    @NotNull
    @Column(name="site_name")
    private String siteName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    @JsonManagedReference(value="employer-movement") // https://stackoverflow.com/questions/20119142/jackson-multiple-back-reference-properties-with-name-defaultreference
    private List<Vacancy> vacancies;

    public Employer(Integer id, String name, String address, String siteName) {
        super(id);
        this.name = name;
        this.address = address;
        this.siteName = siteName;
    }

    public Employer(){}

    public Employer(Employer employer) {
        this(employer.getId(), employer.getName(), employer.getAddress(), employer.getSiteName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public String getSiteName() { return siteName; }

    public void setSiteName(String siteName) { this.siteName = siteName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employer employer = (Employer) o;
        return Objects.equals(name, employer.name) /*&&
                Objects.equals(address, employer.address)&&
                Objects.equals(siteName, employer.siteName)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Employer{" +
                "\nname='" + name + '\'' +
                ", \naddress='" + address + '\'' +
                ", \nsiteName='" + siteName + '\'' +
                ", \nid=" + id +
                '}';
    }
}
