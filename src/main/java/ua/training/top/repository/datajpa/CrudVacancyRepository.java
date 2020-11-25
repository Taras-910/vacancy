package ua.training.top.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Vacancy;

@Transactional(readOnly = true)
public interface CrudVacancyRepository extends JpaRepository<Vacancy, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vacancy v WHERE v.employer.id=:employerId")
    int deleteEmployerVacancies(@Param("employerId") int employerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vacancy v WHERE v.id=:id AND v.employer.id=:employerId")
    int delete(@Param("id") int id, @Param("employerId") int employerId);

    @Query("SELECT v FROM Vacancy v WHERE v.id=:id AND v.employer.id=:employerId")
    Vacancy get(@Param("id") int id, @Param("employerId") int employerId);
}

