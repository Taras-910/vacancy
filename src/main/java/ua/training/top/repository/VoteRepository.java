package ua.training.top.repository;

import ua.training.top.model.Vote;

import java.util.List;

public interface VoteRepository {

    Vote get(int id, int userId);

    List<Vote> getAll();

    List<Vote> getAllForAuth(int userId);

    Vote save(Vote vote, int userId);

    boolean delete(int id, int userId);

    boolean deleteByVacancyId(int vacancyId, int authUserId);

    boolean deleteListByVacancyId(int vacancyId);

    void deleteList(List<Vote> listToDelete);

    void deleteExceed(int limitVotesToKeep);
}
