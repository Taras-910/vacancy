package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Vote;
import ua.training.top.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Logger log = LoggerFactory.getLogger("");
    private final CrudVoteRepository voteRepository;
    private final CrudUserRepository userRepository;

    public DataJpaVoteRepository(CrudVoteRepository voteRepository, CrudUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Vote save(Vote vote, int userID) {
        log.info("vote {}", vote);
        vote.setUserId(userRepository.getOne(userID).getId());
        return vote.isNew() || get(vote.id(), vote.getUserId()) != null ? voteRepository.save(vote) : null;
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) { return voteRepository.delete(id, userId) != 0; }

    @Transactional
    @Override
    public boolean deleteByVacancyId(int vacancyId, int authUserId) {
        return voteRepository.deleteByVacancyId(vacancyId, authUserId) != 0;
    }

    @Override
    public Vote get(int id, int userId) {
        Vote vote = voteRepository.findById(id).orElse(null);
        return vote != null && vote.getUserId() == userId ? vote : null;
    }

    @Override
    public List<Vote> getAllForAuth(int userId) {
        return Optional.ofNullable(voteRepository.getAllForAuth(userId)).orElse(null);
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll();
    }

    @Transactional
    @Override
    public boolean deleteListByVacancyId(int vacancyId) {
        try {
            return Optional.of(voteRepository.deleteListByVacancyId(vacancyId) != 0).orElse(false);
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public void deleteList(List<Vote> listToDelete) {
        voteRepository.deleteAll(listToDelete);
    }

    @Transactional
    @Override
    public void deleteOutDated(LocalDate reasonLocalDateTime) {
        deleteList(voteRepository.getOutDated(reasonLocalDateTime));
    }

    @Transactional
    @Override
    public void deleteExceedLimit(int limitVote) {
        if (getAll().size() > limitVote) {
            deleteList(voteRepository.findExceeded(limitVote));
        }
    }
}
