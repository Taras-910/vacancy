package ua.training.top.aggregator.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.to.DoubleTo;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.List;

import static ua.training.top.util.VacancyUtil.getTestList;

public class TestStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(TestStrategy.class);
    @Override
    public List<VacancyTo> getVacancies(DoubleTo doubleString) throws IOException {
        log.info("getVacancies language={} workplace={}", doubleString.getLanguageTask(), doubleString.getWorkplaceTask());
        return getTestList();
    }
}
