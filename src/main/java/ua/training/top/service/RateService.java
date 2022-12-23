package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.training.top.aggregator.Provider;
import ua.training.top.model.AbstractBaseEntity;
import ua.training.top.model.Rate;
import ua.training.top.repository.RateRepository;
import ua.training.top.util.ValidationUtil;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ua.training.top.aggregator.InstallationUtil.baseCurrency;
import static ua.training.top.aggregator.InstallationUtil.reasonValidRate;
import static ua.training.top.util.MessagesUtil.not_be_null;
import static ua.training.top.util.ValidationUtil.*;

@Service
public class RateService extends AbstractBaseEntity implements Serializable {
    private final static Logger log = LoggerFactory.getLogger(RateService.class);
    public static final Map<String, Rate> mapRates = new ConcurrentHashMap<>();

    @Autowired
    private RateRepository repository;

    public Rate get(int id) {
        log.info("get by id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public Rate getByName(String name) {
        log.info("get by name {}", name);
        return checkNotFound(repository.getByName(name), name);
    }

    @Cacheable(value = "rate")
    public List<Rate> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @CacheEvict(value = "rate", allEntries = true)
    public Rate create(Rate rate) {
        log.info("create {}", rate);
        Assert.notNull(rate, not_be_null);
        checkNew(rate);
        ValidationUtil.validate(rate);
        return repository.save(rate);
    }

    @CacheEvict(value = "rate", allEntries = true)
    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @CacheEvict(value = "rate", allEntries = true)
    public void deleteAll() {
        log.info("deleteAll");
        repository.deleteAll();
    }

    @CacheEvict(value = "rate", allEntries = true)
    public void updateRateDB() {
        log.info("update rate by baseCurrency {}", baseCurrency);
        List<Rate> ratesNew = Provider.getRates();
        if(!ratesNew.isEmpty() && repository != null){
            repository.updateAll(ratesNew);
        }
    }

    public boolean CurrencyRatesMapInit() {
        log.info("currency rates map init \n{}", ": <|> ".repeat(20));
        getAll().forEach(r -> mapRates.put(r.getName(), r));
        Rate rate = mapRates.values().stream().min(Comparator.comparing(Rate::getDateRate)).orElse(null);
        return rate == null || rate.getDateRate().isBefore(reasonValidRate);
    }
}
