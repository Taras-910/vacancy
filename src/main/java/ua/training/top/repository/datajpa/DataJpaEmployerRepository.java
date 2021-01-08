package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.repository.EmployerRepository;

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
    public Employer getOrCreate(Employer employer) {
        Employer employerDb = null;
        try {
            employerDb = repository.getByNameAndAddress(employer.getName(), employer.getAddress());
        } catch (Exception e) {}
        return employerDb == null ? save(employer) : employerDb;
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
    public boolean delete(int id) {
        return Optional.of(repository.delete(id)).orElse(0) != 0;
    }

    @Transactional
    @Override
    public void deleteAllEmpty(int size) {
        repository.deleteAllEmpty(0);
    }
}
