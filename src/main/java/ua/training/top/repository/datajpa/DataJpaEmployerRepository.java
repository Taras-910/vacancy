package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.EmployerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public class DataJpaEmployerRepository implements EmployerRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CrudEmployerRepository repository;

    public DataJpaEmployerRepository(CrudEmployerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employer get(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Employer> getAll() {
        return Optional.of(repository.getAll()).orElse(null);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return Optional.of(repository.delete(id)).orElse(0) != 0;
    }

    @Transactional
    @Override
    public void deleteAllEmpty(int size) {
        repository.deleteAllEmpty(0);
    }

    @Transactional
    @Override
    public Employer save(Employer employer) {
        log.info("employer {}", employer);
        if(employer.isNew()){
            return repository.save(employer);
        }
        return get(employer.getId()) != null ? repository.save(employer) : null;
    }

    @Transactional
    @Override
    public List<Employer> saveList(List<Employer> employers) {
        return repository.saveAll(employers);
    }

    @Transactional
    @Override
    public Employer getOrCreate(Employer employer) {
        List<Employer> listByName = null;
        Employer doubleEmployer = null;
        try {
            listByName = new ArrayList<>(repository.getByName(employer.getName()));
            log.info("listByName {}", listByName);
        } catch (Exception e) {}
        if (listByName == null || listByName.isEmpty()) {
            employer.setVacancies(null);
            return save(employer);
        }
        try {
            for (Employer tempEmployer : listByName) {
                Vacancy vacancy = tempEmployer.getVacancies().stream()
                        .filter(v -> v.getTitle().equals(employer.getVacancies().get(0).getTitle())
                                && v.getSkills().equals(employer.getVacancies().get(0).getSkills())).findFirst().orElse(null);
                doubleEmployer = vacancy == null ? null : tempEmployer;
            }
        } catch (Exception e) {
            return listByName.get(0);
        }
        employer.setVacancies(null);
        if (doubleEmployer == null) {
            return listByName.get(0);
        }
        repository.delete(doubleEmployer.getId());
        return save(employer);
    }
}
