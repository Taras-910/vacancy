package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.training.top.aggregator.AggregatorController;
import ua.training.top.model.Freshen;
import ua.training.top.repository.FreshenRepository;

import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.ValidationUtil.*;

@Service
public class FreshenService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final FreshenRepository repository;
    @Autowired
    private AggregatorController aggregatorController;

    public FreshenService(FreshenRepository repository) {
        this.repository = repository;
    }

    public Freshen get(int id) {
        log.info("get by id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Freshen> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public Freshen create(Freshen freshen) {
        log.info("create {}", freshen);
        Assert.notNull(freshen, "freshen must not be null");
        checkNew(freshen);
        return repository.save(freshen);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public void update(Freshen freshen, int id) {
        log.info("update {} with id={}", freshen, id);
        assureIdConsistent(freshen, id);
        Assert.notNull(freshen, "freshen must not be null");
        checkNotFoundWithId(repository.save(freshen), freshen.id());
    }
    // use logic for restricting access
    public List <Freshen> getByDoubleString(String workplace, String language) {
        log.info("getByDoubleString workplace={} language={}", workplace, language);
        return repository.getByDoubleString(workplace, language);
    }

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB freshen {}", freshen);
        aggregatorController.refreshDB(asNewFreshen(freshen));
    }
}
