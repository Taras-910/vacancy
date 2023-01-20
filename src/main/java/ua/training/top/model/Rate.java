package ua.training.top.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "rate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Rate  extends AbstractBaseEntity implements Serializable {

    @NotBlank
    @Size(min = 6, max = 6)
    @Column(name = "name")
    private String name;

    @DecimalMax("1000.0") @DecimalMin("0.01")
    @Column(name = "value_rate")
    private Double valueRate;

    @NotNull
    @Column(name = "date_rate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateRate;

    public Rate() {}

    public Rate(Integer id, @NotNull @Size(min = 6, max = 6) String name, @NotNull Double valueRate, LocalDate dateRate) {
        super(id);
        this.name = name;
        this.valueRate = valueRate;
        this.dateRate = dateRate;
    }

    public Rate(Rate r) {
        this(r.id, r.name, r.getValueRate(), r.getDateRate());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValueRate() {
        return valueRate;
    }

    public void setValueRate(Double value) {
        this.valueRate = value;
    }

    public LocalDate getDateRate() { return dateRate; }

    public void setDateRate(LocalDate localDate) { this.dateRate = localDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Objects.equals(name, rate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + valueRate +
                ", dateRate=" + dateRate +
                '}';
    }
}
