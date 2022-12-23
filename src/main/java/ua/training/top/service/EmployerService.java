package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.training.top.model.AbstractBaseEntity;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.EmployerRepository;
import ua.training.top.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.training.top.util.EmployerUtil.checkDataEmployer;
import static ua.training.top.util.MessagesUtil.not_be_null;
import static ua.training.top.util.ValidationUtil.checkNew;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class EmployerService {
    public static final Logger log = LoggerFactory.getLogger(EmployerService.class);

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
        Assert.notNull(employer, not_be_null);
        return repository.getOrCreate(employer);
    }

    public Employer create(Employer employer) {
        log.info("create {}", employer);
        Assert.notNull(employer, not_be_null);
        checkNew(employer);
        ValidationUtil.validate(employer);
        return repository.save(employer);
    }

    public List<Employer> createList(ArrayList<Employer> employers) {
        return repository.createList(employers);
    }

    public void update(Employer employer) {
        Assert.notNull(employer, not_be_null);
        checkDataEmployer(employer);
        checkNotFoundWithId(repository.save(employer), employer.id());
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public void deleteEmpty(List<Vacancy> list) {
        log.info("deleteEmptyEmployers");
        List<Employer> listEmptyEmployers = new ArrayList<>();
        Map<Integer, List<Vacancy>> vacanciesEmployersId = list.stream()
                .collect(Collectors.groupingBy(v -> v.getEmployer().getId()));
        Map<Integer, Employer> employersEmployersId = repository.getAll().stream()
                .collect(Collectors.toMap(AbstractBaseEntity::getId, e -> e));

        employersEmployersId.keySet().forEach( e -> {
            if (!vacanciesEmployersId.containsKey(e)) {
                listEmptyEmployers.add(employersEmployersId.get(e));
            }
        });
        repository.deleteList(listEmptyEmployers);
    }

    public void deleteList(List<Employer> list) {
        repository.deleteList(list);
    }
}
