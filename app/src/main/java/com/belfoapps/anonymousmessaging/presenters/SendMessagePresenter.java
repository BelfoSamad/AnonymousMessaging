package com.belfoapps.anonymousmessaging.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.belfoapps.anonymousmessaging.contracts.SendMessageContract;
import com.belfoapps.anonymousmessaging.ui.views.activities.SendMessageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class SendMessagePresenter implements SendMessageContract.Presenter{
    private static final String TAG = "SendMessagePresenter";
    /***************************************** Declarations ***************************************/
    private SendMessageActivity mView;
    private FirebaseFirestore mDb;

    /***************************************** Constructor ****************************************/
    public SendMessagePresenter(FirebaseFirestore mDb) {
        this.mDb = mDb;
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
}
