package ua.training.top.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toCollection;

@Repository
@Transactional(readOnly = true)
public class JpaVacancyRepository implements VacancyRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Vacancy save(Vacancy vacancy, int employerId) {
        Employer ref = em.getReference(Employer.class, employerId);
        vacancy.setEmployer(ref);
        if (vacancy.isNew()) {
            em.persist(vacancy);
            return vacancy;
        } else {
            Vacancy fromDB = get(vacancy.getId(), employerId);
            return fromDB != null && fromDB.getEmployer().getId() == employerId ? em.merge(vacancy) : null;
        }
    }
    @Override
    @Transactional
    public boolean delete(int id, int employerId) {
        Query query = em.createQuery("DELETE FROM Vacancy v WHERE v.id=:id AND v.employer.id=:employerId");
        return Optional.of(query.setParameter("id", id).setParameter("employerId", employerId).executeUpdate()).orElse(0) != 0;
    }

    @Override
    public Vacancy get(int id, int employerId) {
        Vacancy vacancy = Optional.ofNullable(em.find(Vacancy.class, id)).orElse(null);
        return vacancy == null || vacancy.getEmployer().getId() != employerId ? null : vacancy;
    }

    @Override
    public List<Vacancy> getAll() {
        return em.createNamedQuery(Vacancy.ALL_SORTED, Vacancy.class)
                .getResultList();
    }

    @Override
    @Transactional
    public List<Vacancy> saveAll(List<Vacancy> vacancies, int employerId) {
        return vacancies.stream()
                .map(v -> new Vacancy(save(v, employerId)))
                .collect(toCollection(ArrayList::new));
    }

    @Override
    @Transactional
    public boolean deleteListOfVacancies(int employerId) {
        Query query = em.createQuery("DELETE FROM Vacancy v WHERE v.employer.id=:employerId");
        return Optional.of(query.setParameter("employerId", employerId).setParameter("employerId", employerId).executeUpdate()).orElse(0) != 0;
    }
}
