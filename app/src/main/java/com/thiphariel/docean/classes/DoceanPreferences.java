package com.thiphariel.docean.classes;

import android.content.Context;

/**
 * Created by Thiphariel on 26/09/2015.
 */
public class DoceanPreferences {
    private static final String SHARED_PREFERENCE_FILE_NAME = "docean_pref";

    public static final String API_TOKEN = "api_token";

    /**
     * Allow to save values in shared preferences
     * @param context
     * @param key
     * @param value
     */
    public static void saveToSharedPreference(Context context, String key, String value) {
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Retrieve data from shared preferences
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String readSharedPreference(Context context, String key, String defaultValue) {
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
}
