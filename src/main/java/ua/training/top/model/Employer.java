package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
@NamedQueries({
        @NamedQuery(name = Employer.WITH_VACANCIES, query = "SELECT e FROM Employer e LEFT JOIN FETCH e.vacancies ORDER BY e.name"),
        @NamedQuery(name = Employer.BY_NAME, query = "SELECT e FROM Employer e WHERE e.name=:name"),
        @NamedQuery(name = Employer.ALL_SORTED, query = "SELECT e FROM Employer e ORDER BY e.name")
})
@Entity
@Table(name = "employer", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "employers_idx")})
public class Employer extends AbstractBaseEntity{
    public static final String WITH_VACANCIES = "Employer.getAllWithVacancies";
    public static final String BY_NAME = "Employer.getByName";
    public static final String ALL_SORTED = "Employer.getAllSorted";

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name="address")
    String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employer", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    private List<Vacancy> vacancies;

    public Employer(Integer id, String name, String address) {
        super(id);
        this.name = name;
        this.address = address;
    }

    public Employer(){}

    public Employer(Employer employer) {
        this(employer.getId(), employer.getName(), employer.getAddress());
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
}
