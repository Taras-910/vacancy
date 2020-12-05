package ua.training.top.aggregator.strategy;

import ua.training.top.to.VacancyNet;

import java.io.IOException;
import java.util.List;

public interface Strategy {

    public List<VacancyNet> getVacancies (String city, String language) throws IOException;

}
