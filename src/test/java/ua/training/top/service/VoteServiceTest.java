package ua.training.top.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.testData.VoteTestData;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Vote;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.testData.TestUtil.NOT_FOUND;
import static ua.training.testData.UserTestData.ADMIN_ID;
import static ua.training.testData.UserTestData.admin;
import static ua.training.testData.VacancyTestData.VACANCY2_ID;
import static ua.training.testData.VoteTestData.*;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.util.DateTimeUtil.thisDay;

class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @BeforeEach
    void setup() {
        setTestAuthorizedUser(admin);
    }

    @Test
    void get() {
        Vote vote = service.get(VOTE1_ID);
        VOTE_MATCHER.assertMatch(vote1, vote);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getAll() {
        List<Vote> all = service.getAllAuth();
        VOTE_MATCHER.assertMatch(allVotes(), all);
    }

    @Test
    void getAllAuth() {
        List<Vote> all = service.getAllAuth();
        VOTE_MATCHER.assertMatch(List.of(vote1), all);
    }

    @Test
    void create() {
        Vote created = service.create(VACANCY2_ID);
        int newId = created.id();
        Vote newVote = new Vote(null, thisDay, VACANCY2_ID, ADMIN_ID);
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(newVote, created);
        VOTE_MATCHER.assertMatch(newVote, service.get(newId));
    }

    @Test
    void update() {
        service.update(VOTE1_ID, VACANCY2_ID);
        Vote expected = service.get(VOTE1_ID);
        VOTE_MATCHER.assertMatch(expected, VoteTestData.getUpdated());
    }

    @Test
    void updateInvalid() {
        assertThrows(NotFoundException.class, () -> service.update(NOT_FOUND, VACANCY2_ID));
    }

    @Test
    void createInvalid() {
        assertThrows(NotFoundException.class, () -> service.update(VACANCY2_ID, NOT_FOUND));
    }
    @Test
    void delete() {
        service.delete(VOTE1_ID);
        assertThrows(NotFoundException.class, () -> service.get(VOTE1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(VOTE2_ID));
    }
}
