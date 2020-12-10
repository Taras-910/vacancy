package ua.training.top.web.jsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ua.training.top.model.Vote;
import ua.training.top.service.VoteService;

import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;

@Controller
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    public Vote get(int id) {
        log.info("get by id {}", id);
        return service.get(id);
    }

    public List<Vote> getAllForAuthUser() {
        log.info("get all for User {}", authUserId());
        return service.getAllForAuthUser(authUserId());
    }

    public List<Vote> getAll() {
        log.info("getAll votes");
        return service.getAll();
    }

    public void delete(int id) {
        log.info("delete vote {}", id);
        service.delete(id);
    }

    public Vote create(int vacancyId) {
        log.info("create vote for employerId {}", vacancyId);
        return service.create(vacancyId);
    }

    public void update(int voteId, int vacancyId) {
        log.info("update vote {} for vacancy {}", voteId, vacancyId);
        service.update(voteId, vacancyId);
    }

    public List<Vote> createAll(List<Vote> newVotes) {
        log.info("createAll newVotes {}", newVotes);
        return service.createAll(newVotes);
    }

    public void deleteAll() {
        log.info("deleteAll");
        service.deleteAll();
    }
}
