package com.belfoapps.anonymousmessaging.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.belfoapps.anonymousmessaging.contracts.MessagesContract;
import com.belfoapps.anonymousmessaging.pojo.Message;
import com.belfoapps.anonymousmessaging.ui.views.activities.MessagesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagesPresenter implements MessagesContract.Presenter {
    private static final String TAG = "MessagesPresenter";
    /***************************************** Declarations ***************************************/
    private MessagesActivity mView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private FirebaseUser mUser;
    private ArrayList<Message> messages;

    /***************************************** Constructor ****************************************/
    public MessagesPresenter(FirebaseAuth mAuth, FirebaseFirestore mDb) {
        this.mAuth = mAuth;
        this.mDb = mDb;
        mUser = mAuth.getCurrentUser();
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(MessagesContract.View view) {
        mView = (MessagesActivity) view;
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
    public void setUserInfo() {
        mView.setUserInfo(mUser.getDisplayName(), mAuth.getUid());
    }

    @Override
    public void copyUid() {
        ClipboardManager clipboard = (ClipboardManager) mView.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Anonymous Messages", mAuth.getUid());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(mView, "UID Copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateProfilePicture() {

    }

    @Override
    public void initRecyclerView() {
        mDb.collection("messages")
                .whereEqualTo("uid", mAuth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        messages = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Message message = document.toObject(Message.class);
                            message.setId(document.getId());
                            messages.add(message);
                        }
                        mView.initRecyclerView(messages);

                        if (messages.size() == 0)
                            mView.showNoMessages();
                        else mView.showMessages();
                    } else {
                        mView.initRecyclerView(messages);
                        mView.showNoNetwork();
                    }
                });
    }

    @Override
    public void updateRecyclerView() {
        mView.updateRecyclerView(messages);

        if (messages == null)
            mView.showNoNetwork();
        else if (messages.size() == 0)
            mView.showNoMessages();
        else mView.showMessages();
    }

    @Override
    public void deleteMessage(Message message) {
        messages.remove(message);
        mDb.collection("messages").document(message.getId())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
        updateRecyclerView();
    }

    @Override
    public void likeMessage(Message message, boolean liked) {
        Map<String, Object> update = new HashMap<>();
        update.put("liked", liked);
        mDb.collection("messages")
                .document(message.getId())
                .update(update).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onComplete: Updating Successful");
            } else Log.d(TAG, "onComplete: " + task.getException());
        });
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }
}
