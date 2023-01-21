package ua.training.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Employer;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;

public class EmployerTestData {
    public static final TestMatcher<Employer> EMPLOYER_MATCHER = TestMatcher.usingFieldsComparator(Employer.class,"vacancies");
    public static final int EMPLOYER1_ID = START_SEQ + 2;
    public static final int EMPLOYER2_ID = EMPLOYER1_ID + 1;
    public static final Employer employer1 = new Employer(EMPLOYER1_ID, "Huuuge Games", "Киев");
    public static final Employer employer2 = new Employer(EMPLOYER2_ID, "RedLab", "Киев");

    public static Employer getNew() {
        return new Employer(null, "Созданный Employer", "NewYork");
    }

    public static Employer getUpdated() {
        return new Employer(EMPLOYER1_ID, "Обновленный Employer", "updatedCity");
    }

}
