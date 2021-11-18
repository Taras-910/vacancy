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
    public boolean deleteAllEmpty(int size) {
        return repository.deleteAllEmpty(0) != 0;
    }

    @Transactional
    @Override
    public List<Employer> createList(ArrayList<Employer> employers) {
        List<Employer> employersDb = new ArrayList<>();
        try {
            employersDb = repository.saveAll(employers);
        } catch (Exception e) {
            for(Employer employer : employers) {
                log.error("error " + employer + " redirect on method save");
                employersDb.add(save(employer));
            }
        }
        return employersDb;
    }

    @Transactional
    @Override
    public Employer save(Employer employer) {
        log.info("save employer {}", employer);
        if(employer.isNew()){
            return repository.save(employer);
        }
        return get(employer.getId()) != null ? repository.save(employer) : null;
    }

    @Transactional
    @Override
    public Employer getOrCreate(Employer employer) {
        List<Employer> listByName = null;
        Employer doubleEmployer = null;
        try {
            listByName = new ArrayList<>(repository.getByName(employer.getName()));
        } catch (Exception ignored) {}

        if (listByName == null || listByName.isEmpty()) {
            employer.setVacancies(null);
            return save(employer);
        }
        try {
            for (Employer e : listByName) {
                Vacancy vacancy = e.getVacancies().stream()
                        .filter(v -> v.getTitle().equals(employer.getVacancies().get(0).getTitle())
                                && v.getSkills().equals(employer.getVacancies().get(0).getSkills())).findFirst().orElse(null);
                if (vacancy != null) {
                    doubleEmployer = e;
                }
            }
        } catch (Exception e) {
            return listByName.get(0);
        }
        return doubleEmployer == null ? listByName.get(0) : doubleEmployer;
    }
}
