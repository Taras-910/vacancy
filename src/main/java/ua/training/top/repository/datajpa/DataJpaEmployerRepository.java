package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.training.top.model.Employer;
import ua.training.top.repository.EmployerRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaEmployerRepository implements EmployerRepository {
    private Logger log = LoggerFactory.getLogger(getClass());
    private CrudEmployerRepository repository;

    public DataJpaEmployerRepository(CrudEmployerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employer save(Employer employer) {
        log.info("employer {}", employer);
        if(employer.isNew()){
            return repository.save(employer);
        }
        return getById(employer.getId()) != null ? repository.save(employer) : null;
    }

    @Override
    public boolean delete(int id) {
        return Optional.of(repository.delete(id)).orElse(0) != 0;
    }

    @Override
    public Employer getById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Employer> getAll() {
        return Optional.of(repository.getAll()).orElse(null);
    }

    @Override
    public Employer getByName(String name) {
        return Optional.of(repository.getByName(name)).orElse(null);
    }

    @Override
    public List<Employer> getAllWithVacancies() {
        return Optional.of(repository.getAllWithVacancies()).orElse(null);
    }

    @Override
    public List<Employer> createAll(List<Employer> employers) {
        if(employers != null) log.info("employers {}", employers.size());
//        repository.deleteAll();
        return repository.saveAll(employers);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
