package ua.training.top.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.training.top.model.Employer;
import ua.training.top.repository.EmployerRepository;

import java.util.ArrayList;
import java.util.List;

import static ua.training.top.util.EmployerUtil.EMPLOYER_NOT_BE_NULL;
import static ua.training.top.util.EmployerUtil.checkDataEmployer;
import static ua.training.top.util.UserUtil.USER_NOT_BE_NULL;
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
        Assert.notNull(employer, USER_NOT_BE_NULL);
        return repository.getOrCreate(employer);
    }

    public Employer create(Employer employer) {
        Assert.notNull(employer, EMPLOYER_NOT_BE_NULL);
        checkDataEmployer(employer);
        return repository.save(employer);
    }

    public List<Employer> createList(ArrayList<Employer> employers) {
        return repository.createList(employers);
    }

    public void update(Employer employer) {
        Assert.notNull(employer, EMPLOYER_NOT_BE_NULL);
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
