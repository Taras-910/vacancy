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
    Vote get(int id);

    // null if not found
    List<Vote> getAllForAuthUser(int userId);

    List<Vote> getAll();

/*
    List<Vote> getBetween(Date startDate, Date endDate, int userId);

    Vote getByDateForAuth(Date localDate, int userId);

    List<Vote> getByEmployer(int employerId);

    List<Vote> getByEmployerAuth(int id, int authUserId);

    List<Vote> getByDate(Date localDate);
*/
}
