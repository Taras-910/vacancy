package ua.training.top.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Vacancy;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudVacancyRepository extends JpaRepository<Vacancy, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vacancy v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vacancy v WHERE v.id=:id AND v.employer.id=:employerId")
    Vacancy get(@Param("id") int id, @Param("employerId") int employerId);

    @Query("SELECT v FROM Vacancy v ORDER BY v.releaseDate DESC")
    List<Vacancy> getAll();

    @Query("SELECT v FROM Vacancy v WHERE v.title=:title AND v.skills=:skills AND v.employer.id=:employerId")
    Vacancy getByParams(@Param("title")String title, @Param("skills")String skills, @Param("employerId") int employerId);

    @Query("SELECT v FROM Vacancy v WHERE (LOWER(v.title) LIKE CONCAT('%',:language,'%') " +
            "OR LOWER(v.skills) LIKE CONCAT('%',:language,'%')) " +
            "AND LOWER(v.employer.address) LIKE CONCAT('%',:workplace,'%') ORDER BY v.title DESC")
    List<Vacancy> getByFilter(@Param("language")String language, @Param("workplace")String workplace);
}
