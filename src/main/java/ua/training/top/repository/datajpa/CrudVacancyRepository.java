package ua.training.top.repository.datajpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Vacancy;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVacancyRepository extends JpaRepository<Vacancy, Integer>, PagingAndSortingRepository<Vacancy, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vacancy v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vacancy v WHERE v.id=:id AND v.employer.id=:employerId")
    Vacancy get(@Param("id") int id, @Param("employerId") int employerId);

    @Query("SELECT v FROM Vacancy v WHERE v.title=:title AND v.skills=:skills AND v.employer.id=:employerId")
    Vacancy getByParams(@Param("title")String title, @Param("skills")String skills, @Param("employerId") int employerId);

    @Query("SELECT v FROM Vacancy v WHERE " +
            "(LOWER(v.title) LIKE CONCAT('%',:language,'%') " +
            "OR LOWER(v.skills) LIKE CONCAT('%',:language,'%')) " +
            "AND (LOWER(v.title) LIKE CONCAT('%',:level,'%')" +
            "OR LOWER(v.skills) LIKE CONCAT('%',:level,'%'))")
    List<Vacancy> getByFilter(@Param("language")String language, @Param("level")String level);

    @Query("SELECT v FROM Vacancy v WHERE v.releaseDate<:reasonPeriodToKeep")
    List<Vacancy> getOutDated(@Param("reasonPeriodToKeep") LocalDate reasonPeriodToKeep);

    /*https://stackoverflow.com/questions/9314078/setmaxresults-for-spring-data-jpa-annotation*/
    @Query(value = "SELECT * FROM Vacancy v ORDER BY v.release_date ASC , v.id ASC LIMIT :number", nativeQuery = true)
    List<Vacancy> getOutNumber(@Param("number") int number);

    @Query(value = "SELECT v FROM Vacancy v ORDER BY v.releaseDate DESC, v.id DESC")
    Page<Vacancy> getPage(PageRequest pageable);
}


