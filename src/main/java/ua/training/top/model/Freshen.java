package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ua.training.top.util.xss.SafeFromXssUtil.getXssCleaned;

@Entity
@Table(name = "freshen")
public class Freshen extends AbstractBaseEntity implements Serializable {

    @NotNull
    @Column(name = "recorded_date", nullable = false)
    private LocalDateTime recordedDate;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name="language")
    private String language;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name="workplace")
    private String workplace;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "freshen"/*, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH}*/)
    @JsonManagedReference(value="freshen-movement") //https://stackoverflow.com/questions/20119142/jackson-multiple-back-reference-properties-with-name-defaultreference
    private List<Vacancy> vacancies;

    public Freshen(Integer id, LocalDateTime recordedDate, String language, String workplace, Integer userId) {
        super(id);
        this.recordedDate = recordedDate;
        this.language = language == null ? "" : getXssCleaned(language).toLowerCase();
        this.workplace = workplace == null ? "" : getXssCleaned(workplace).toLowerCase();
        this.userId = userId;
    }

    public Freshen() {
    }

    public Freshen(Freshen f){
        this(f.getId(), f.recordedDate, f.language, f.workplace, f.userId);
    }

    public LocalDateTime getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(LocalDateTime recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }

    @Override
    public String toString() {
        return "Freshen{" +
                "id=" + id +
                ", recordedDate=" + recordedDate +
                ", language='" + language + '\'' +
                ", workplace='" + workplace + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Freshen freshen = (Freshen) o;

        return recordedDate.equals(freshen.recordedDate) && language.equals(freshen.language) && workplace.equals(freshen.workplace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordedDate, language, workplace);
    }
}
