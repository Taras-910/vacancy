package ua.training.top.repository;

import ua.training.top.model.Vote;

import java.util.List;

public interface VoteRepository {

    // null if not found, when updated
    // null if not found
    Vote get(int id, int userId);

    List<Vote> getAll();

    // null if not found
    List<Vote> getAllForAuth(int userId);

    List<Vote> getAllByVacancyId(Integer vacancyId);

    Vote save(Vote vote, int userId);

    List<Vote> saveList(List<Vote> votes);

    // false if not found
    boolean delete(int id, int userId);

    boolean deleteByVacancyId(int vacancyId, int authUserId);

    boolean deleteListByVacancyId(int vacancyId);

}
