package ua.training.top.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Vote;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.userId=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.vacancyId=:vacancyId AND v.userId=:userId")
    int deleteByVacancyId(@Param("vacancyId") int vacancyId, @Param("userId") int authUserId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.vacancyId=:vacancyId")
    int deleteListByVacancyId(@Param("vacancyId") int vacancyId);

    @Query("SELECT v FROM Vote v WHERE v.userId=:userId")
    List<Vote> getAllForAuth(@Param("userId") int userId);

    @Query(value =
            "SELECT * FROM vacancy.public.vote v ORDER BY v.local_date, v.id LIMIT :limit", nativeQuery = true)
    List<Vote> findExceeded(@Param("limit") int limit);
}
