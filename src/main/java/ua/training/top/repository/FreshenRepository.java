package ua.training.top.repository;

import ua.training.top.model.Freshen;

import java.util.List;

public interface FreshenRepository {
    // null if not found, when updated
    Freshen save(Freshen freshen);

    // false if not found
    boolean delete(int id);

    // null if not found
    Freshen get(int id);

    List<Freshen> getAll();

    List <Freshen> getByDoubleString(String workplace, String language);
}
