package ua.dp.strahovik.yalantistask1.util;


import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static long getDateDiff(Date oldestDate, Date newestDate, TimeUnit timeUnit) {
        long diffInMillis = newestDate.getTime() - oldestDate.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
