package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Vote;

import java.util.Arrays;
import java.util.List;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.testData.EmployerTestData.EMPLOYER1_ID;
import static ua.training.top.testData.EmployerTestData.EMPLOYER2_ID;
import static ua.training.top.testData.UserTestData.ADMIN_ID;
import static ua.training.top.testData.UserTestData.USER_ID;
import static ua.training.top.util.DateTimeUtil.thisDay;
import static ua.training.top.util.DateTimeUtil.toDate;

public class VoteTestData {
    public static TestMatcher<Vote> VACANCY_MATCHER = TestMatcher.usingFieldsComparator();
    public static final int VOTE1_ID = START_SEQ + 6;
    public static final int VOTE2_ID = VOTE1_ID + 1;
    public static final Vote VOTE1 = new Vote(VOTE1_ID,  toDate(2020, 6, 28), EMPLOYER1_ID, ADMIN_ID);
    public static final Vote VOTE2 = new Vote(VOTE2_ID,  toDate(2020, 6, 29), EMPLOYER2_ID, USER_ID);

    public static Vote getNew() {
        return new Vote(null, thisDay, EMPLOYER2_ID, ADMIN_ID);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE1_ID, thisDay, EMPLOYER2_ID, ADMIN_ID);
    }

    public static List<Vote> allVotes(){
        return Arrays.asList(VOTE1, VOTE2);
    }

    public static List<Vote> allForEmployer(){
        return Arrays.asList(VOTE1);
    }

    public static List<Vote> allForAuth(){
        return Arrays.asList(VOTE2);
    }

    public static List<Vote> allForAdmin(){
        return Arrays.asList(VOTE1);
    }

    public static List<Vote> between(){
        return Arrays.asList(VOTE1, VOTE2);
    }
}
