package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Rate;

import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static ua.training.top.model.AbstractBaseEntity.START_SEQ;

public class RateTestData {
    public static TestMatcher<Rate> RATE_MATCHER = TestMatcher.usingFieldsComparator(Rate.class);
    public static final int RATE1_ID = START_SEQ + 10;
    public static final int RATE2_ID = RATE1_ID + 1;
    public static final Rate rate1 = new Rate(RATE1_ID,  "USDEUR", 1.02021, of(2020,10,25));
    public static final Rate rate2 = new Rate(RATE2_ID,  "USDGBP", 0.89565, of(2020,10,25));

    public static Rate getNew() {
        return new Rate(null, "USDUAH", 36.9300, now());
    }

    public static Rate getUpdated() {
        return new Rate(null,"USDGBP",0.897, now());
    }

    public static List<Rate> allRate(){
        return Arrays.asList(rate1, rate2);
    }

}
