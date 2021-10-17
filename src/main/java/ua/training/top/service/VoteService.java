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
import static ua.training.top.util.VoteUtil.checkNotFoundData;

@Service
public class VoteService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote get(int id) {
        int userId = authUserId();
        log.info("get by id {} for user {}", id, userId);
        Vote vote = repository.get(id, userId);
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
        checkNotFoundData(repository.deleteListByVacancyId(vacancyId), vacancyId);
    }

        @Transactional
    public void deleteList(List<Vote> listToDelete) {
        log.info("deleteList");
        if (!listToDelete.isEmpty()) {
            repository.deleteList(listToDelete);
        }
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
