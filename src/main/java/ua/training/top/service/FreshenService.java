package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.Freshen;
import ua.training.top.repository.FreshenRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.MessageUtil.not_be_null;
import static ua.training.top.util.ValidationUtil.*;

@Service
@EnableScheduling
public class FreshenService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private FreshenRepository repository;
    @Autowired
    private AggregatorService aggregatorService;

    public Freshen get(int id) {
        log.info("get by id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Freshen> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @Transactional
    public Freshen create(Freshen freshen) {
        log.info("create {}", freshen);
        Assert.notNull(freshen, not_be_null);
        checkNew(freshen);
        return repository.save(asNewFreshen(freshen));
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public void update(Freshen freshen, int id) {
        log.info("update {} with id={}", freshen, id);
        assureIdConsistent(freshen, id);
        Assert.notNull(freshen, not_be_null);
        checkNotFoundWithId(repository.save(freshen), freshen.id());
    }

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB freshen {}", freshen);
        aggregatorService.refreshDB(freshen);
    }

    public void deleteExceedLimit(int limitFreshensToKeep) {
        log.info("deleteExceedLimit limitFreshensToKeep={}", limitFreshensToKeep);
        repository.deleteExceedLimit(limitFreshensToKeep);
    }

    public void deleteOutDated(LocalDateTime outPeriodToKeep) {
        log.info("deleteOutDated outPeriodToKeep={}", outPeriodToKeep);
        repository.deleteOutDated(outPeriodToKeep);
    }
}
