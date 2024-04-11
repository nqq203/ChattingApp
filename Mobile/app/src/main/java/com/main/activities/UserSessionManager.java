package com.main.activities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class UserSessionManager {
    public static final String PREFERENCES_NAME = "UserSessionPreferences";
    public static final String KEY_IS_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_PHONE_NUMBER = "PhoneNumber";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserSessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createUserLoginSession(String phoneNumber) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_PHONE_NUMBER, prefs.getString(KEY_PHONE_NUMBER, null));
        user.put(KEY_IS_LOGGED_IN, String.valueOf(prefs.getBoolean(KEY_IS_LOGGED_IN, false)));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
