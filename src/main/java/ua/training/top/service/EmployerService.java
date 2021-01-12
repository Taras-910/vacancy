package ua.training.top.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.training.top.model.Employer;
import ua.training.top.repository.EmployerRepository;

import java.util.List;

import static ua.training.top.util.ValidationUtil.checkDataEmployer;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class EmployerService {

    private final EmployerRepository repository;

    public EmployerService(EmployerRepository repository) {
        this.repository = repository;
    }

    public Employer get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Employer> getAll() {
        return repository.getAll();
    }

    public Employer getOrCreate(Employer employer) {
        Assert.notNull(employer, "user must not be null");
        return repository.getOrCreate(employer);
    }

    public Employer create(Employer employer) {
        Assert.notNull(employer, "employer must not be null");
        Employer employerDB = repository.save(employer);
        return employerDB;
    }

    public List<Employer> createList(List<Employer> employers) {
        employers.forEach(employer -> Assert.notNull(employer, "user must not be null"));
        return repository.saveList(employers);
    }

    public void update(Employer employer) {
        Assert.notNull(employer, "user must not be null");
        checkDataEmployer(employer);
        checkNotFoundWithId(repository.save(employer), employer.id());
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public void deleteEmptyEmployers() {
        repository.deleteAllEmpty(0);
    }

}
