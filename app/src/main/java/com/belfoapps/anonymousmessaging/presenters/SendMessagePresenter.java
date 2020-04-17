package com.belfoapps.anonymousmessaging.presenters;

import android.util.Log;

import com.belfoapps.anonymousmessaging.contracts.SendMessageContract;
import com.belfoapps.anonymousmessaging.models.SharedPreferencesHelper;
import com.belfoapps.anonymousmessaging.ui.views.activities.SendMessageActivity;
import com.belfoapps.anonymousmessaging.utils.GDPR;
import com.google.android.gms.ads.AdView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class SendMessagePresenter implements SendMessageContract.Presenter {
    private static final String TAG = "SendMessagePresenter";
    /***************************************** Declarations ***************************************/
    private SendMessageActivity mView;
    private FirebaseFirestore mDb;
    private SharedPreferencesHelper mSharedPrefs;
    private GDPR gdpr;

    /***************************************** Constructor ****************************************/
    public SendMessagePresenter(FirebaseFirestore mDb, SharedPreferencesHelper mSharedPrefs, GDPR gdpr) {
        this.mDb = mDb;
        this.mSharedPrefs = mSharedPrefs;
        this.gdpr = gdpr;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(SendMessageContract.View view) {
        mView = (SendMessageActivity) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    @Override
    public boolean isDarkModeEnabled() {
        return mSharedPrefs.isDarkModeEnabled();
    }

    /***************************************** Methods ********************************************/

    @Override
    public void checkUserExist(String uid) {
        mDb.collection("users")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            users.add(document.getString("username"));
                        }

                        if (users.size() == 0)
                            mView.showUserNotFound();
                        else {
                            mView.showSendMessage(users.get(0));
                        }
                    } else {
                        mView.showNoNetwork();
                    }
                });
    }


    @Override
    public void sendMessage(String uid, String message) {
        HashMap<String, Object> data = new HashMap<>();

        data.put("uid", uid);
        data.put("message", message);
        data.put("liked", false);

        mDb.collection("messages")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "sendMessage: Succeeded");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "sendMessage: Failed");
                });
    }

    @Override
    public void loadAd(AdView ad) {
        if (mSharedPrefs.isAdPersonalized()) {
            gdpr.showPersonalizedAdBanner(ad);
        } else {
            gdpr.showNonPersonalizedAdBanner(ad);
        }
    }
}
