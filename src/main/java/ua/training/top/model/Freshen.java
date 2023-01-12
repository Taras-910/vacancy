package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.util.aggregatorUtil.xss.XssUtil.xssClear;

@Entity
@Table(name = "freshen")
public class Freshen extends AbstractBaseEntity implements Serializable {

    @Column(name = "recorded_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime recordedDate;

    @NotEmpty
    @Size(min = 2, max = 100)
    @Column(name = "language")
    private String language;

    @NotEmpty
    @Size(min = 2, max = 100)
    @Column(name = "level")
    private String level;

    @NotEmpty
    @Size(min = 2, max = 100)
    @Column(name = "workplace")
    private String workplace;

    @Column(name = "goal")
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "freshen_goal", joinColumns = @JoinColumn(name = "freshen_id"))
//    @Fetch(FetchMode.SUBSELECT)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Goal> goals;

    @Column(name = "user_id")
    private Integer userId;

    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "freshen")
    private List<Vacancy> vacancies;

    public Freshen(Integer id, LocalDateTime recordedDate, String language, String level, String workplace, Collection<Goal> goals, Integer userId) {
        this(id, recordedDate, language, level, workplace, userId);
        setGoals((Set<Goal>) goals);
    }

    public Freshen(Freshen f){
        this(f.getId(), f.recordedDate, f.language, f.level, f.workplace, f.getGoals(), f.userId);
    }

    public Freshen(Integer id, LocalDateTime recordedDate, String language, String level, String workplace, Integer userId){
        super(id);
        this.recordedDate = recordedDate;
        this.language = hasText(language) ? xssClear(language).toLowerCase() : "all";
        this.level = hasText(level) ? xssClear(level).toLowerCase() : "all";
        this.workplace = hasText(workplace) ? xssClear(workplace).toLowerCase() : "all";
        this.userId = userId;
    }

    public Freshen() {
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
        this.language = xssClear(language).toLowerCase();
    }

    public String getLevel() { return level; }

    public void setLevel(String level) { this.level = level; }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = xssClear(workplace).toLowerCase();
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

    public Set<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    @Override
    public String toString() {
        return "Freshen{" +
                "id=" + id +
                ", recordedDate=" + recordedDate +
                ", language='" + language + '\'' +
                ", level='" + level + '\'' +
                ", workplace='" + workplace + '\'' +
                ", goals=" + goals +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Freshen freshen = (Freshen) o;

        return language.equalsIgnoreCase(freshen.language)
                && level.equalsIgnoreCase(freshen.level )
                && workplace.equalsIgnoreCase(freshen.workplace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language.toLowerCase(), level.toLowerCase(), workplace.toLowerCase());
    }
}
