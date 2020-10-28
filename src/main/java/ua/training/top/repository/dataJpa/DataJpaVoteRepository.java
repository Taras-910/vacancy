package ua.training.top.repository.dataJpa;

import org.springframework.stereotype.Repository;
import ua.training.top.model.Vote;
import ua.training.top.repository.VoteRepository;

import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    @Override
    public Vote save(Vote vote, int userId) {
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return false;
    }

    @Override
    public Vote get(int id, int userId) {
        return null;
    }

    @Override
    public Vote get(int id) {
        return null;
    }

    @Override
    public List<Vote> getAllForAuthUser(int userId) {
        return null;
    }

    @Override
    public List<Vote> getAll() {
        return null;
    }
}
