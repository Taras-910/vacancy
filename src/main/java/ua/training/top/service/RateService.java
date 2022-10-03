package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.Rate;
import ua.training.top.repository.RateRepository;

import java.util.List;

import static ua.training.top.aggregator.InstallationUtil.baseCurrency;
import static ua.training.top.aggregator.Provider.getRates;
import static ua.training.top.util.MessageUtil.not_be_null;
import static ua.training.top.util.ValidationUtil.checkNew;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RateService {
    private final static Logger log = LoggerFactory.getLogger(RateService.class);

    @Autowired
    private RateRepository repository;

    public Rate get(int id) {
        log.info("get by id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public Rate getByName(String name) {
        log.info("get by name {}", name);
//        return checkNotFound(repository.getByName(name), name);
        System.out.println("repository.equals(null)="+(repository == null));
        return repository.getByName(name);
    }

    public List<Rate> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @Transactional
    public Rate create(Rate rate) {
        log.info("create {}", rate);
        Assert.notNull(rate, not_be_null);
        checkNew(rate);
        return repository.save(rate);
    }

    @Transactional
    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Transactional
    public void updateRate() {
        log.info("update rate by baseCurrency {}", baseCurrency);
        List<Rate> ratesNew = getRates();
        List<Rate> ratesDB = getAll();
        ratesNew.forEach(ratesDB::remove);
        if(!ratesDB.isEmpty()){
            ratesNew.addAll(ratesDB);
        }
        repository.deleteAll();
        repository.saveAll(ratesNew);
    }
}
