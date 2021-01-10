package ua.training.top.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

import static ua.training.top.util.xss.SafeFromXssUtil.getXssCleaned;

@Entity
@Table(name = "freshen")
public class Freshen extends AbstractBaseEntity {

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

    public void setRecordedDate(LocalDateTime freshenDateTime) {
        this.recordedDate = freshenDateTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? "" : getXssCleaned(language).toLowerCase();
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace == null ? "" : getXssCleaned(workplace).toLowerCase();
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
