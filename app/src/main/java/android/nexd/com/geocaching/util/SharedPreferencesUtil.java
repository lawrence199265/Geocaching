package android.nexd.com.geocaching.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lawrence on 2015/10/12.
 */
public class SharedPreferencesUtil {

    public static SharedPreferences getLastVisitMall(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("last_visit_mall", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void setLastVisitMall(Context context, String cityName, String mallName, String floorName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("last_visit_mall", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("city", cityName);
        edit.putString("mall", mallName);
        edit.putString("floorName", floorName);
        edit.apply();
    }


//    public static void setAutoSignIn(Context context, int i) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("is_auto", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("is_auto", i);
//        editor.apply();
//    }
//
//    public static int isAutoSignIn(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("is_auto", Context.MODE_PRIVATE);
//        return sharedPreferences.getInt("is_auto", 1);
//    }

    public static void saveAccountPasswordAutoSignIn(Context context, String account, String password, int isAutoSign) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("save_password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", account);
        editor.putString("password", password);
        editor.putInt("is_auto", isAutoSign);
        editor.apply();
    }

    public static SharedPreferences getAccountPasswordAutoSignIn(Context context) {
        return context.getSharedPreferences("save_password", Context.MODE_PRIVATE);
    }
}
