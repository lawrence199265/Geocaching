package android.nexd.com.geocaching.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xu on 2015/8/14.
 */
public class DateUtil {
    public static String MMdd_HH_mm = "MMdd_HH:mm";
    public static String YYYYMMDDhhmmss = "yyyyMMddHHmmss";

    public static String getCurrentByType(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(new Date());
    }
}
