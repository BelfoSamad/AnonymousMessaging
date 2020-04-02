package com.belfoapps.anonymousmessaging.models;

import android.content.SharedPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class SharedPreferencesHelper {
    private static final String DARK_MODE = "Dark Mode";
    private static final String SAVED = "Saved";
    private static final String SHOPPING = "Shopping";
    private static final String LAST_UPDATE = "Last Update";

    /************************************* Declarations *******************************************/
    private SharedPreferences sharedPref;
    private Gson gson;

    /************************************* Constructor ********************************************/
    public SharedPreferencesHelper(SharedPreferences sharedPref, Gson gson) {
        this.sharedPref = sharedPref;
        this.gson = gson;
    }

    /************************************* Methods ***********************************************/

    private boolean isEmpty(String mode) {
        return sharedPref.getString(mode, null) == null;
    }

    /*
    public void saveRecipes(ArrayList<Recipe> recipes) {
        String jsonToString = gson.toJson(recipes);
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putString(SAVED, jsonToString).apply();
    }

    public ArrayList<Recipe> getSavedRecipes() {
        if (isEmpty(SAVED)) return null;
        else {
            String jsonRecipes = sharedPref.getString(SAVED, null);
            Recipe[] recipeItem = gson.fromJson(jsonRecipes, Recipe[].class);
            return new ArrayList<>(Arrays.asList(recipeItem));
        }
    }
    */

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
}
