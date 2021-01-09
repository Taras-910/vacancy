package ua.training.top.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

import static ua.training.top.util.xss.SafeFromXssUtil.getXssCleaned;

@Entity
@Table(name = "freshen")
public class Freshen extends AbstractBaseEntity {

    @Column(name = "freshen_date_time", nullable = false)
    private LocalDateTime freshenDateTime;

    @NotNull
    @Column(name="language")
    private String language;

    @NotNull
    @Column(name="workplace")
    private String workplace;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public Freshen(Integer id, LocalDateTime freshenDateTime, String language, String workplace, Integer userId) {
        super(id);
        this.freshenDateTime = freshenDateTime;
        this.language = language == null ? "" : getXssCleaned(language).toLowerCase();
        this.workplace = workplace == null ? "" : getXssCleaned(workplace).toLowerCase();
        this.userId = userId;
    }

    public Freshen() {
    }

    public Freshen(Freshen f){
        this(f.getId(), f.freshenDateTime, f.language, f.workplace, f.userId);
    }

    public LocalDateTime getFreshenDateTime() {
        return freshenDateTime;
    }

    public void setFreshenDateTime(LocalDateTime freshenDateTime) {
        this.freshenDateTime = freshenDateTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? "" : getXssCleaned(language);
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace == null ? "" : getXssCleaned(workplace);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Freshen{" +
                "id=" + id +
                ", freshenDateTime=" + freshenDateTime +
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

        return freshenDateTime.equals(freshen.freshenDateTime) && language.equals(freshen.language) && workplace.equals(freshen.workplace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(freshenDateTime, language, workplace);
    }
}
