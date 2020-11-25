package ua.training.top.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.Assert;
import ua.training.top.model.Employer;
import ua.training.top.repository.EmployerRepository;
import ua.training.top.util.exception.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static ua.training.top.util.ValidationUtil.checkNotFound;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class EmployerService {

    private final EmployerRepository repository;

    public EmployerService(EmployerRepository repository) {
        this.repository = repository;
    }

    public Employer getById(int id) {
        return checkNotFoundWithId(repository.getById(id), id);
    }

    public List<Employer> getAll() {
        return repository.getAll();
    }

    public Employer getByName(String name) {
        return checkNotFound(repository.getByName(name), "name=" + name);
    }

    public List<Employer> getAllWithVacancies() {
        return repository.getAllWithVacancies();
    }

    public void update(@Valid @NotEmpty Employer employer) {
        try {
            Assert.notNull(employer, "user must not be null");
            checkNotFoundWithId(repository.save(employer), employer.id());
        } catch (IllegalArgumentException | TransactionSystemException | DataIntegrityViolationException e) {
            throw new NotFoundException("error data employer=" + employer);
        }
    }

    public Employer create(@Valid @NotEmpty Employer employer) {
        Assert.notNull(employer, "user must not be null");
        return repository.save(employer);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
