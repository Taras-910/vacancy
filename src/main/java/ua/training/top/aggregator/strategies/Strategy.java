package ua.training.top.aggregator.strategies;

import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.List;

public interface Strategy {
    List<VacancyTo> getVacancies (Freshen doubleString) throws IOException;
}
