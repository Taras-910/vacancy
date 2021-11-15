package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoteUtil {
    public static Logger log = LoggerFactory.getLogger(VoteUtil.class);

    public static void checkNotFoundData(boolean found, Object id) {
        if (!found) {
            log.error("Not found entity with " + id);
        }
    }
}
