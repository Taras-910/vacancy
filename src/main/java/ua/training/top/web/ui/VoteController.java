package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import ua.training.top.model.Vote;
import ua.training.top.repository.VoteRepository;

import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.DateTimeUtil.thisDay;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteRepository repository;

    public VoteController(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote get(int id) {
        log.info("get by id {} for user {}", id, authUserId());
        Vote vote = repository.get(id, authUserId());
        return checkNotFoundWithId(vote, id);
    }

    public List<Vote> getAllForAuthUser(int userId) {
        log.info("get all for User {}", userId);
        return repository.getAllForAuthUser(userId);
    }

    public List<Vote> getAll() {
        log.info("getAll votes");
        return repository.getAll();
    }

    public void delete(int id) {
        log.info("delete vote {} for userId {}", id, authUserId());
        checkNotFoundWithId(repository.delete(id, authUserId()), id);
    }

    public Vote create(int vacancyId) {
        log.info("create vote for employerId {}", vacancyId);
        Vote vote = new Vote(null, thisDay, vacancyId, authUserId());
        return repository.save(vote, authUserId());
    }

    public void update(int voteId, int vacancyId) {
        log.info("update vote {} for employerId {} of user {}", voteId, vacancyId, authUserId());
        Vote vote = new Vote(voteId, thisDay, vacancyId, authUserId());
        Assert.notNull(vote, "vote must not be null");
        checkNotFoundWithId(repository.save(vote, authUserId()), voteId);
    }
}