package Utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

public class Time {

    public static LocalDateTime toUTC(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime
                .atZone(zoneId)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    public static LocalDateTime fromUTC(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime
                .atZone(ZoneOffset.UTC)
                .withZoneSameInstant(zoneId)
                .toLocalDateTime();
    }

    public static ZoneId getZoneId(String location) {

        switch(location)
        {
            case "New York Office" :
                return ZoneId.of("America/New_York");
            case "Phoenix Office" :
                return ZoneId.of("America/Phoenix");
            case "London Office" :
                return ZoneId.of("Europe/London");
            default :
                return null;
        }
    }

    public static String getDateString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public static String getTimeString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    public static Timestamp now() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }

    public static long dateTimeDifference(Temporal d1, Temporal d2, ChronoUnit unit){
        return unit.between(d1, d2);
    }

    public static boolean validTimeSlot(String startTime, String endTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalTime t1 = LocalTime.parse(startTime, timeFormatter);
        LocalTime t2 = LocalTime.parse(endTime, timeFormatter);
        long diff = dateTimeDifference(t1, t2, ChronoUnit.HOURS);
        return (diff > 0);
    }
}
