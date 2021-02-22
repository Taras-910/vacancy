package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.training.top.model.Freshen;
import ua.training.top.repository.FreshenRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ua.training.top.util.DateTimeUtil.tomorrow;
import static ua.training.top.util.DateTimeUtil.yesterday;
import static ua.training.top.util.FreshenUtil.FRESHEN_NOT_BE_NULL;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
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

    public Freshen create(Freshen freshen) {
        log.info("create {}", freshen);
        Assert.notNull(freshen, FRESHEN_NOT_BE_NULL);
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
        Assert.notNull(freshen, FRESHEN_NOT_BE_NULL);
        checkNotFoundWithId(repository.save(freshen), freshen.id());
    }

    public List <Freshen> getBetween(LocalDateTime endDate, LocalDateTime startDate) {
        log.info("getToday endDate {} startDate {}", endDate, startDate);
        return repository.getBetween(endDate, startDate);
    }

    @Async
    public void refreshDB(Freshen freshen) {
        log.info("refreshDB freshen {}", freshen);
//        checkLimitFreshenPerHour(freshen, getBetween(tomorrow, yesterday));
        aggregatorService.refreshDB(freshen);
    }

    public Freshen getLast() {
        log.info("getLast");
        return repository.getBetween(tomorrow, yesterday).stream().max((f1, f2) -> f1.getId().compareTo(f2.getId())).get();
    }
}
