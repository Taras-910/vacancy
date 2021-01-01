package ua.training.top.aggregator.strategy;

import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.List;

import static ua.training.top.util.VacancyUtil.getTestList;

public class Test implements Strategy{
    @Override
    public List<VacancyTo> getVacancies(DoubleString doubleString) throws IOException {
        return getTestList();
    }
}
