package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.training.top.model.Vote;
import ua.training.top.repository.VoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Logger log = LoggerFactory.getLogger("");
    private final CrudVoteRepository voteRepository;
    private final CrudUserRepository userRepository;

    public DataJpaVoteRepository(CrudVoteRepository voteRepository, CrudUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Vote save(Vote vote, int userID) {
        log.info("vote {}", vote);
        vote.setUserId(userRepository.getOne(userID).getId());
        return vote.isNew() || get(vote.id(), vote.getUserId()) != null ? voteRepository.save(vote) : null;
    }

    @Override
    public List<Vote> saveList(List<Vote> votes) {
        return voteRepository.saveAll(votes);
    }

    @Override
    public boolean delete(int id, int userId) { return voteRepository.delete(id, userId) != 0; }

    @Override
    public Vote get(int id, int userId) {
        Vote vote = voteRepository.findById(id).orElse(null);
        return vote != null && vote.getUserId() == userId ? vote : null;
    }

    @Override
    public List<Vote> getAllForAuthUser(int userId) {
        return Optional.ofNullable(voteRepository.getAllForAuthUser(userId)).orElse(null);
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll();
    }

    @Override
    public boolean deleteByVacancyId(int vacancyId, int authUserId) {
        return voteRepository.deleteByVacancyId(vacancyId, authUserId) != 0;
    }

    @Override
    public boolean deleteListByVacancyId(int vacancyId) {
        return voteRepository.deleteListByVacancyId(vacancyId) != 0;
    }

    @Override
    public List<Vote> getAllByVacancyId(Integer vacancyId) {
        return Optional.of(voteRepository.getAllByVacancyId(vacancyId)).orElse(new ArrayList<Vote>());
    }

}
