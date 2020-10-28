package ua.training.top.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.Assert;
import ua.training.top.model.Employer;
import ua.training.top.repository.EmployerRepository;
import ua.training.top.util.exception.NotFoundException;

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

    public void update(Employer employer) {
        try {
            Assert.notNull(employer, "user must not be null");
            checkNotFoundWithId(repository.save(employer), employer.id());
        } catch (IllegalArgumentException | TransactionSystemException | DataIntegrityViolationException e) {
            throw new NotFoundException("error data employer=" + employer);
        }
    }
//        assertThrows(IllegalArgumentException.class, () -> service.update(null));
//        assertThrows(TransactionSystemException.class, () -> service.update(new Employer(EMPLOYER1_ID, null, "newAddress")));
//        assertThrows(DataIntegrityViolationException.class, () -> service.update(new Employer(EMPLOYER1_ID, "Новый", null)));
    public Employer create(Employer employer) {
        Assert.notNull(employer, "user must not be null");
        return repository.save(employer);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
