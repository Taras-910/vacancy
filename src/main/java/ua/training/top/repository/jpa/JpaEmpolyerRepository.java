package ua.training.top.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.repository.EmployerRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public class JpaEmpolyerRepository implements EmployerRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Employer save(Employer employer) {
        if (employer.isNew()) {
            em.persist(employer);
            return employer;
        } else {
            return em.merge(employer);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        Query query = em.createQuery("DELETE FROM Employer e WHERE e.id=:id");
        return query.setParameter("id", id).executeUpdate() != 0;    }

    @Override
    public Employer getById(int id) {
        return em.find(Employer.class, id);
    }

    @Override
    public List<Employer> getAll() {
        return em.createNamedQuery(Employer.ALL_SORTED, Employer.class).getResultList();
    }

    @Override
    public Employer getByName(String name) {
        log.info("name {}", name);
        return em.createNamedQuery(Employer.BY_NAME, Employer.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public List<Employer> getAllWithVacancies() {
        return em.createNamedQuery(Employer.WITH_VACANCIES, Employer.class).getResultList();
    }
}
