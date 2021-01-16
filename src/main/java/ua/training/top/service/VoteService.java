package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.Vote;
import ua.training.top.repository.VoteRepository;

import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.DateTimeUtil.thisDay;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote get(int id) {
        log.info("get by id {} for user {}", id, authUserId());
        Vote vote = repository.get(id, authUserId());
        return checkNotFoundWithId(vote, id);
    }

    public List<Vote> getAll() {
        log.info("getAll votes");
        return repository.getAll();
    }

    public List<Vote> getAllForAuth() {
        log.info("get all for User {}", authUserId());
        return repository.getAllForAuth(authUserId());
    }

    @Transactional
    public Vote create(int vacancyId) {
        log.info("create vote for employerId {}", vacancyId);
        Vote vote = new Vote(null, thisDay, vacancyId, authUserId());
        return repository.save(vote, authUserId());
    }

    public List<Vote> createList(List<Vote> votes) {
        return repository.saveList(votes);
    }

    @Transactional
    public void update(int voteId, int vacancyId) {
        log.info("update vote {} for vacancyId {} of user {}", voteId, vacancyId, authUserId());
        Vote vote = new Vote(voteId, thisDay, vacancyId, authUserId());
        Assert.notNull(vote, "vote must not be null");
        checkNotFoundWithId(repository.save(vote, authUserId()), voteId);
    }

    @Transactional
    public void delete(int id) {
        log.info("delete vote {} for userId {}", id, authUserId());
        checkNotFoundWithId(repository.delete(id, authUserId()), id);
    }

    @Transactional
    public void deleteListByVacancyId(int vacancyId) {
        repository.deleteListByVacancyId(vacancyId);
    }

    @Transactional
    public void setVote(int vacancyId, boolean toVote) {
        log.info(toVote ? "enable {}" : "disable {}", vacancyId);
        if (!toVote){
            log.info("deleteByVacancyId {}", vacancyId);
            checkNotFoundWithId(repository.deleteByVacancyId(vacancyId, authUserId()), vacancyId);
        } else {
            log.info("create vote for vacancyId {} userId {}", vacancyId, authUserId());
            create(vacancyId);
        }
    }
}
