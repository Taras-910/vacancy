package ua.training.top.repository;

import ua.training.top.model.Employer;

import java.util.ArrayList;
import java.util.List;

public interface EmployerRepository {
    // null if not found
    Employer get(int id);

    List<Employer> getAll();

    Employer getOrCreate(Employer employer);

    // null if not found, when updated
    Employer save(Employer employer);

    // false if not found
    boolean delete(int id);

    List<Employer> createList(ArrayList<Employer> employers);

    void deleteList(List<Employer> list);
}
