package android.nexd.com.geocaching.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lawrence on 2015/10/29.
 */
public class JsonUtil {

    public static JSONObject getJSONObject (String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }
}
