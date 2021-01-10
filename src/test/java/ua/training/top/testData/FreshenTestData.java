package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Freshen;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.testData.UserTestData.ADMIN_ID;
import static ua.training.top.testData.UserTestData.USER_ID;

public class FreshenTestData {
    public static TestMatcher<Freshen> FRESHEN_MATCHER = TestMatcher.usingFieldsComparator(Freshen.class,"recordedDate");

    public static final int FRESHEN1_ID = START_SEQ + 8;
    public static final int FRESHEN2_ID = FRESHEN1_ID + 1;
    public static final Freshen freshen1 = new Freshen(FRESHEN1_ID, of(2020, 10, 25, 12,0), "java", "киев", ADMIN_ID);
    public static final Freshen freshen2 = new Freshen(FRESHEN2_ID, of(2020, 10, 25, 13,0), "java", "киев", USER_ID);

    public static Freshen getNew() {
        return new Freshen(null, now(), "java", "newCity",ADMIN_ID);
    }

    public static Freshen getUpdated() {
        Freshen updated = new Freshen(freshen1);
        updated.setLanguage("javascript");
        updated.setWorkplace("UpdatedCity");
        return updated;
    }
}
