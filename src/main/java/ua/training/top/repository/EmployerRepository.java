package ua.training.top.repository;

import ua.training.top.model.Employer;

import java.util.List;

public interface EmployerRepository {
    // null if not found, when updated
    Employer save(Employer employer);

    // false if not found
    boolean delete(int id);

    // null if not found
    Employer getById(int id);

    List<Employer> getAll();

    Employer getByName(String name);

    List<Employer> createAll(List<Employer> employers);

    void deleteEmptyEmployers(int size);
}
