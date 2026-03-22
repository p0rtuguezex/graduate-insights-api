package pe.com.graduate.insights.api.shared.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class DateUtils {

  private static final String DATE_PATTERN = "yyyy-MM-dd";

  private DateUtils() {}

  public static Date now() {
    return new Date();
  }

  public static boolean areDifferentDates(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      return false;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
    String date1Str = sdf.format(date1);
    String date2Str = sdf.format(date2);
    return !date1Str.equals(date2Str);
  }

  public static boolean isSecondDateAfterFirstDate(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      throw new IllegalArgumentException("Both dates must be non-null");
    }
    LocalDate currentLocalDate = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate compareLocalDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return compareLocalDate.isBefore(currentLocalDate);
  }

  public static String dateToString(Date date) {
    if (date == null) {
      return StringUtils.EMPTY;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
    return sdf.format(date);
  }

  public static Date stringToDate(String dateString) {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
    Date date;
    try {
      date = sdf.parse(dateString);
    } catch (ParseException e) {
      throw new IllegalArgumentException("El formato de fecha debe ser yyyy-MM-dd");
    }
    return date;
  }
}
