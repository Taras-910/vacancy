package ua.training.top.repository;

import ua.training.top.model.Vote;

import java.util.List;

public interface VoteRepository {

    // null if not found, when updated
    Vote save(Vote vote, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Vote get(int id, int userId);

    // null if not found
    List<Vote> getAllForAuthUser(int userId);

    List<Vote> getAll();

    List<Vote> saveAll(List<Vote> newVotes);

    void deleteAll();
}
