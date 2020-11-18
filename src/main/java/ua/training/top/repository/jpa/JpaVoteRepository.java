package ua.training.top.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Vote;
import ua.training.top.repository.VoteRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaVoteRepository implements VoteRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Vote save(Vote vote, int userId) {
        log.info("vote {} userId {}", vote, userId);
        if (vote.isNew()) {
            em.persist(vote);
            return vote;
        } else {
            Vote fromDB = get(vote.getId(), userId);
            return fromDB != null && fromDB.getUserId() == userId ? em.merge(vote) : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Query query = em.createQuery("DELETE FROM Vote v WHERE v.id=:id and v.userId=:userId");
        return query.setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Vote get(int id, int userId) {
        Query query = em.createQuery("SELECT v FROM Vote v WHERE v.id=:id and v.userId=:userId");
        return (Vote) query.setParameter("id", id)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public List<Vote> getAllForAuthUser(int userId) {
        Query query = em.createQuery("SELECT v FROM Vote v WHERE v.userId=:userId");
        return query.setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Vote> getAll() {
        Query query = em.createQuery("SELECT v FROM Vote v");
        return query.getResultList();
    }
}

