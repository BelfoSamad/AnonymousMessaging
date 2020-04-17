package com.belfoapps.anonymousmessaging.models;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferencesHelper {
    private static final String DARK_MODE = "Dark Mode";
    private static final String PERSONALIZED_ADS = "AD";

    /************************************* Declarations *******************************************/
    private SharedPreferences sharedPref;
    private Gson gson;

    /************************************* Constructor ********************************************/
    public SharedPreferencesHelper(SharedPreferences sharedPref, Gson gson) {
        this.sharedPref = sharedPref;
        this.gson = gson;
    }

    /************************************* Extra Methods ******************************************/
    //Dark Mode
    public void setDarkModeEnable(boolean isEnabled) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(DARK_MODE, isEnabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return sharedPref.getBoolean(DARK_MODE, false);
    }

    public void setAdPersonalized(boolean isPersonalized) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(PERSONALIZED_ADS, isPersonalized).apply();
    }

    public boolean isAdPersonalized() {
        return sharedPref.getBoolean(PERSONALIZED_ADS, false);
    }
}
