package ua.training.top.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.AbstractTest;
import ua.training.top.model.Vote;
import ua.training.top.testData.VoteTestData;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.TestUtil.NOT_FOUND;
import static ua.training.top.testData.UserTestData.ADMIN_ID;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.VacancyTestData.VACANCY2_ID;
import static ua.training.top.testData.VoteTestData.*;
import static ua.training.top.util.DateTimeUtil.thisDay;

public class VoteServiceTest extends AbstractTest {

    @Autowired
    VoteService service;
    @Before
    public void setup() {
        setTestAuthorizedUser(admin);
    }

    @Test
    public void get() throws Exception {
        Vote vote = service.get(VOTE1_ID);
        VOTE_MATCHER.assertMatch(vote1, vote);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getAll() throws Exception {
        List<Vote> all = service.getAllForAuth();
        VOTE_MATCHER.assertMatch(allVotes(), all);
    }

    @Test
    public void getAllForAuthUser() throws Exception {
        List<Vote> all = service.getAllForAuth();
        VOTE_MATCHER.assertMatch(List.of(vote1), all);
    }

    @Test
    public void delete() throws Exception {
        service.delete(VOTE1_ID);
        assertThrows(NotFoundException.class, () -> service.get(VOTE1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(VOTE2_ID));
    }

    @Test
    public void update() throws Exception {
        service.update(VOTE1_ID, VACANCY2_ID);
        Vote expected = service.get(VOTE1_ID);
        VOTE_MATCHER.assertMatch(expected, VoteTestData.getUpdated());
    }

    @Test
    public void updateIllegalArgument() throws Exception {
        assertThrows(NotFoundException.class, () -> service.update(NOT_FOUND, VACANCY2_ID));
    }

    @Test
    public void create() throws Exception {
        Vote created = service.create(VACANCY2_ID);
        int newId = created.id();
        Vote newVote = new Vote(null, thisDay, VACANCY2_ID, ADMIN_ID);
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(newVote, created);
        VOTE_MATCHER.assertMatch(newVote, service.get(newId));
    }
}
