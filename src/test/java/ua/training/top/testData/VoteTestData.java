package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Vote;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.testData.UserTestData.ADMIN_ID;
import static ua.training.top.testData.UserTestData.USER_ID;
import static ua.training.top.testData.VacancyTestData.VACANCY1_ID;
import static ua.training.top.testData.VacancyTestData.VACANCY2_ID;
import static ua.training.top.util.DateTimeUtil.thisDay;

public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsComparator(Vote.class,"localDate");
    public static final int VOTE1_ID = START_SEQ + 8;
    public static final int VOTE2_ID = VOTE1_ID + 1;
    public static final Vote vote1 = new Vote(VOTE1_ID,  LocalDate.of(2020, 10, 25), VACANCY1_ID, ADMIN_ID);
    public static final Vote vote2 = new Vote(VOTE2_ID,  LocalDate.of(2020, 10, 25), VACANCY2_ID, USER_ID);

    public static Vote getNew() {
        return new Vote(null, thisDay, VACANCY2_ID, ADMIN_ID);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE1_ID, thisDay, VACANCY2_ID, ADMIN_ID);
    }

    public static List<Vote> allVotes(){
        return Arrays.asList(vote1);
    }

    public static List<Vote> allForEmployer(){
        return Arrays.asList(vote1);
    }

    public static List<Vote> allForAuth(){
        return Arrays.asList(vote2);
    }

    public static List<Vote> allForAdmin(){
        return Arrays.asList(vote1);
    }

    public static List<Vote> between(){
        return Arrays.asList(vote1, vote2);
    }
}
