package ua.training.top.web.ui;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Vote;
import ua.training.top.testData.VoteTestData;
import ua.training.top.util.exception.NotFoundException;

import javax.persistence.NoResultException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThrows;
import static ua.training.top.testData.UserTestData.ADMIN_ID;
import static ua.training.top.testData.UserTestData.NOT_FOUND;
import static ua.training.top.testData.VacancyTestData.VACANCY2_ID;
import static ua.training.top.testData.VoteTestData.*;
import static ua.training.top.util.DateTimeUtil.thisDay;

public class VoteControllerTest extends AbstractServiceTest {

    @Autowired
    VoteController controller;

    @Test
    public void get() throws Exception {
        Vote vote = controller.get(VOTE1_ID);
        VOTE_MATCHER.assertMatch(VOTE1, vote);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NoResultException.class, () -> controller.get(NOT_FOUND));
    }

    @Test
    public void getAll() throws Exception {
        List<Vote> all = controller.getAll();
        VOTE_MATCHER.assertMatch(allVotes(), all);
    }

    @Test
    public void getAllForAuthUser() throws Exception {
        List<Vote> all = controller.getAllForAuthUser(ADMIN_ID);
        VOTE_MATCHER.assertMatch(asList(VOTE1), all);
    }

    @Test
    public void delete() throws Exception {
        controller.delete(VOTE1_ID);
        assertThrows(NoResultException.class, () -> controller.get(VOTE1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(VOTE2_ID));
    }

    @Test
    public void update() throws Exception {
        controller.update(VOTE1_ID, VACANCY2_ID);
        Vote expected = controller.get(VOTE1_ID);

        VOTE_MATCHER.assertMatch(expected, VoteTestData.getUpdated());
    }

//    @Test
//    public void updateNotOwn() throws Exception {
//        assertThrows(NotFoundException.class, () -> controller.update(VOTE1_ID, VACANCY1_ID));
//    }

    @Test
    public void updateIllegalArgument() throws Exception {
        assertThrows(NoResultException.class, () -> controller.update(NOT_FOUND, VACANCY2_ID));
    }

    @Test
    public void create() throws Exception {
        Vote created = controller.create(VACANCY2_ID);
        int newId = created.id();
        Vote newVote = new Vote(null, thisDay, VACANCY2_ID, ADMIN_ID);
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(newVote, created);
        VOTE_MATCHER.assertMatch(newVote, controller.get(newId));
    }
}
