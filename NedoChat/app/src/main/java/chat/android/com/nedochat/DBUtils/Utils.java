package chat.android.com.nedochat.DBUtils;

import android.content.Context;
import android.content.SharedPreferences;


public class Utils {
    private static Utils sUtils;
    private SharedPreferences sharedPref;

    private static final String KEY_SHARED_PREF = "ANDROID_CHAT";
    private static final int KEY_MODE_PRIVATE = 0;
    private static final String KEY_SESSION_USER = "sessionUser";

    public Utils(Context context) {
        sharedPref = context.getSharedPreferences(KEY_SHARED_PREF,
                KEY_MODE_PRIVATE);
    }

    public static Utils get(Context c) {
        if (sUtils == null) {
            sUtils = new Utils(c.getApplicationContext());
        }
        return sUtils;
    }


    public void storeSessionUser(String user) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_SESSION_USER, user);
        editor.apply();
    }

    public String getSessionUser() {
        return sharedPref.getString(KEY_SESSION_USER, null);
    }

    public boolean logoutSessionUser() {
        sharedPref.edit().putString(KEY_SESSION_USER, null).apply();
        return true;
    }
}
