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
    @Query("DELETE FROM Vote v WHERE v.id=:id And v.userId=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.vacancyId=:vacancyId And v.userId=:userId")
    int deleteByVacancyId(@Param("vacancyId") int vacancyId, @Param("userId") int authUserId);

    @Query("SELECT v FROM Vote v WHERE v.userId=:userId")
    List<Vote> getAllForAuthUser(@Param("userId") int userId);
}
