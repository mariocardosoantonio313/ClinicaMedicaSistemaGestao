package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe utilitária para conversão e formatação de datas.
 */
public class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private DateUtil() {
        // Construtor privado.
    }

    public static String formatDateTime(Date date) {
        return date == null ? "" : DATE_FORMAT.format(date);
    }

    public static String formatDate(Date date) {
        return date == null ? "" : SHORT_DATE_FORMAT.format(date);
    }

    public static Date parseDateTime(String value) throws ParseException {
        return DATE_FORMAT.parse(value);
    }

    public static Date parseDate(String value) throws ParseException {
        return SHORT_DATE_FORMAT.parse(value);
    }
}
