package ua.training.top.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "employer", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "address"}, name = "employers_idx")})
public class Employer extends AbstractBaseEntity{

    @NotBlank
    @Column(name = "name")
    @Size(max = 255)
    private String name;

    @NotBlank
    @Column(name = "address")
    @Size(min = 1, max = 255)
    private String address;

    @ApiModelProperty(hidden = true)
//    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employer")
//    @JsonManagedReference(value="employer-movement") // https://stackoverflow.com/questions/20119142/jackson-multiple-back-reference-properties-with-name-defaultreference
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    private List<Vacancy> vacancies;

    public Employer(){}

    public Employer(Integer id, String name, String address) {
        super(id);
        this.name = name;
        this.address = address;
    }

    public Employer(Employer e) {
        this(e.getId(), e.getName(), e.getAddress());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employer employer = (Employer) o;
        return Objects.equals(name, employer.name) &&
                Objects.equals(address, employer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Override
    public String toString() {
        return "Employer{" +
                "\nname='" + name + '\'' +
                ", \naddress='" + address + '\'' +
                ", \nid=" + id +
                '}';
    }
}
