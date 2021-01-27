package ua.training.top.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;

import java.util.List;

public interface CrudFreshenRepository extends JpaRepository<Freshen, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Freshen f WHERE f.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT f FROM Freshen f WHERE f.workplace=:workplace AND f.language=:language")
    List<Freshen> getByDoubleString(@Param("workplace") String workplace, @Param("language") String language);

    @Query("SELECT f FROM Freshen f WHERE f.userId=:userId")
    List<Freshen> getAllAuth(@Param("userId") int userId);
}
