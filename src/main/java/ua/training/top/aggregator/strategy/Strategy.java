package ua.training.top.aggregator.strategy;

import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.List;

public interface Strategy {

    public List<VacancyTo> getVacancies (DoubleString doubleString) throws IOException;
}
