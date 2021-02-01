package ua.training.top.repository;

import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.util.List;

public interface AggregatorInterface {
    List<VacancyTo> selectBy(Freshen freshen);
}
