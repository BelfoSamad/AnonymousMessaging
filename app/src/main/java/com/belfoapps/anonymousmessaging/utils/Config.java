package com.belfoapps.anonymousmessaging.utils;

import android.content.SharedPreferences;

import com.belfoapps.anonymousmessaging.R;


public class Config {
    public static final String INSTAGRAM_USERNAME = "adiltaouil";
    public static final String FACEBOOK_PAGE = "https://www.facebook.com/%D9%85%D8%B7%D8%A8%D8%AE-%D8%A7%D9%85-%D9%88%D9%84%D9%8A%D8%AF-1627151147543413";
    public static final String TWITTER_ID = "pewdiepie";
    public static final String YOUTUBE_CHANNEL_ID = "UCVXD2kNki3rfLMhF8uNIcBQ";
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";

    private static final String GDPR = "Gdpr";
    private static final String BANNER = "Banner";
    private static final String INTERSTITIAL = "Interstitial";
    private static final String PUBLISHER = "Publisher";
    private static final String BANNER_ID = "Banner ID";
    private static final String INTERSTITIAL_ID = "Interstitial ID";

    private SharedPreferences preferences;

    public Config(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    /************************************** Basics ************************************************/
    public static String[] tabTitles = {"Login", "Register"};

    public void setAdBannerId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(BANNER_ID, id).apply();
    }

    public void setAdInterstitialId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(INTERSTITIAL_ID, id).apply();
    }

    public String getAdBannerId() {
        return preferences.getString(BANNER_ID, "test");
    }

    public String getAdInterstitialId() {
        return preferences.getString(INTERSTITIAL_ID, "test");
    }

    /************************************ Developer Info ******************************************/

    public void setDeveloperName(String name) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(NAME, name).apply();
    }

    public void setDeveloperEmail(String email) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(EMAIL, email).apply();
    }

    public String getDeveloperName() {
        return preferences.getString(NAME, "Developer");
    }

    public String getDeveloperEmail() {
        return preferences.getString(EMAIL, "developer@gmail.com");
    }

    /************************************** Configs ***********************************************/

    public void setBannerEnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(BANNER, enabled).apply();
    }

    public void setInterstitialEnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(INTERSTITIAL, enabled).apply();
    }

    public void setGDPREnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(GDPR, enabled).apply();
    }

    public void setPublisherId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(PUBLISHER, id).apply();
    }

    public boolean isBannerEnabled() {
        return preferences.getBoolean(BANNER, true);
    }

    public boolean isInterstitialEnabled() {
        return preferences.getBoolean(INTERSTITIAL, true);
    }

    public boolean isGDPREnabled() {
        return preferences.getBoolean(GDPR, true);
    }

    public String getPublisherId() {
        //TODO: Remove After Testings
        return preferences.getString(PUBLISHER, "pub-4679171106713552");
    }
}
