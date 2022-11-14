package ua.training.top;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ContextConfiguration(value = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db-test.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@Transactional
@ActiveProfiles("hsqldb")
public abstract class AbstractTest {
    private final static Logger log = LoggerFactory.getLogger("");
    public static final String l = String.format("%s%s","\n",".".repeat(30));
    public static final String header = String.format("%s%s%s%s%s", l,"\nresult"," ".repeat(22), "ms",l);
    public static List<String> listResult = new ArrayList<>(List.of(header));

    @Rule
    public Stopwatch stopwatch = new Stopwatch(){
        @Override
        protected void finished(long nanos, Description description) {
            String message = String.format("\n%-25s%s%3d", description.getMethodName(), "  ", TimeUnit.NANOSECONDS.toMillis(nanos));
            listResult.add(message);
            log.info(message);
        }
    };

    @AfterClass
    public static void after() {
        log.info("{}{}",listResult.toString(),l);
        listResult.clear();
    }
}
