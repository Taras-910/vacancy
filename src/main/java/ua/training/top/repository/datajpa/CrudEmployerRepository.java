package ua.training.top.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudEmployerRepository extends JpaRepository<Employer, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Employer e WHERE e.id=:id")
    int delete(@Param("id") int id);

    @Modifying
    @Query("SELECT e FROM Employer e ORDER BY e.name ASC")
    List<Employer> getAll();

    @Query("SELECT DISTINCT e FROM Employer e LEFT JOIN FETCH e.vacancies v ORDER BY e.name ASC")
    List<Employer> getAllWithVacancies();

    @Query("SELECT e FROM Employer e WHERE e.name=:name")
    Employer getByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM Employer e WHERE e.vacancies.size=:size")
    void deleteEmptyEmployers(@Param("size") int size);
}
