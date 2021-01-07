package ua.training.top.aggregator.strategy;

import ua.training.top.to.DoubleTo;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.List;

public interface Strategy {

    public List<VacancyTo> getVacancies (DoubleTo doubleString) throws IOException;
}
