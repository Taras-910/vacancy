package ua.training.top.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "vacancy_id"}, name = "votes_idx")})
public class Vote extends AbstractBaseEntity {

    @Column(name = "local_date", nullable = false)
    @NotNull
    private Date localDate;

    @Column(name = "vacancy_id", nullable = false)
    @NotNull
    private Integer vacancyId;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Integer userId;

    public Vote(){}

    public Vote(Integer id, @NotNull Date localDate, @NotNull Integer vacancyId, @NotNull Integer userId) {
        super(id);
        this.localDate = localDate;
        this.vacancyId = vacancyId;
        this.userId = userId;
    }

    public Vote(Vote l) {
        this(l.getId(), l.getLocalDate(), l.getVacancyId(), l.getUserId());
    }

    public Date getLocalDate() { return localDate; }

    public void setLocalDate(Date date) { this.localDate = date; }

    public Integer getVacancyId() { return vacancyId; }

    public void setVacancyId(Integer vacancyId) { this.vacancyId = vacancyId; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer user) { this.userId = user; }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", localDate=" + localDate +
                ", vacancyId=" + vacancyId +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;

        return userId.equals(vote.getUserId()) && vacancyId.equals(vote.getVacancyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(vacancyId, userId);
    }
}
