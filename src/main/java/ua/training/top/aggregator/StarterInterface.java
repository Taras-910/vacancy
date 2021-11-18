package ua.training.top.aggregator;

import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.util.List;

public interface StarterInterface {
    List<VacancyTo> selectBy(Freshen freshen);
}
