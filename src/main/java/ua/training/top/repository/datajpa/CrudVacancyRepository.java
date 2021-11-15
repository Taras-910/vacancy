package ua.training.top.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Vacancy;

import java.time.LocalDate;
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

    @Query("SELECT v FROM Vacancy v WHERE " +
            "(LOWER(v.title) LIKE CONCAT('%',:language,'%') " +
            "OR LOWER(v.skills) LIKE CONCAT('%',:language,'%')) " +
            "AND (LOWER(v.title) LIKE CONCAT('%',:level,'%')" +
            "OR LOWER(v.skills) LIKE CONCAT('%',:level,'%'))" +
            "AND (LOWER(v.employer.address) LIKE CONCAT('%',:workplace,'%') " +
            "OR v.freshen.workplace LIKE CONCAT('%',:workplace,'%'))")
    List<Vacancy> getByFilter(@Param("language")String language, @Param("level")String level, @Param("workplace")String workplace);

    @Query("SELECT v FROM Vacancy v WHERE v.releaseDate=:localDate")
    List<Vacancy> getCountToday(@Param("localDate")LocalDate localDate);

    @Query("SELECT v FROM Vacancy v WHERE v.freshen.id=:id")
    List<Vacancy> getByFreshenId(@Param("id") Integer id);

    @Query("SELECT v FROM Vacancy v WHERE v.releaseDate<:reasonPeriodToKeep")
    List<Vacancy> getOutDated(@Param("reasonPeriodToKeep") LocalDate reasonPeriodToKeep);

    //    https://stackoverflow.com/questions/9314078/setmaxresults-for-spring-data-jpa-annotation
    @Query(value =
            "SELECT * FROM vacancy.public.vacancy v ORDER BY v.release_date, v.id LIMIT :exceedNumber", nativeQuery = true)
    List<Vacancy> findExceeded(@Param("exceedNumber") int exceedNumber);
}
