package udpm.hn.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static Long convertDateToTimeStampSecond(Date startDate) {
        if (startDate != null) {
            return startDate.getTime() / 1000;
        }
        return null;
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Long getCurrentTimeStampSecond() {
        return System.currentTimeMillis() / 1000;
    }

    public static Long parseStringToLong(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }

        // Nếu là timestamp dạng chuỗi số 13 chữ số
        if (dateString.matches("\\d{13}")) {
            try {
                return Long.parseLong(dateString);
            } catch (NumberFormatException ignored) {
            }
        }

        if (dateString.matches("\\d+")) {
            try {
                int excelSerial = Integer.parseInt(dateString);
                LocalDate excelDate = LocalDate.of(1900, 1, 1).plusDays(excelSerial - 2);
                return excelDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            } catch (Exception ignored) {
            }
        }

        // Nếu là định dạng chuỗi ngày thông thường
        String[] formats = { "dd/MM/yyyy", "yyyy-MM-dd", "dd-MM-yyyy" };
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                Date date = sdf.parse(dateString.trim());
                return date.getTime();
            } catch (ParseException ignored) {
            }
        }

        return null;
    }

    public static String parseLongToString(Long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date(date));
    }
}
