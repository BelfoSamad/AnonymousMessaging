package com.belfoapps.anonymousmessaging.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.MessagesContract;
import com.belfoapps.anonymousmessaging.pojo.Message;
import com.belfoapps.anonymousmessaging.ui.views.activities.MessagesActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private FirebaseStorage mStorage;
    private ArrayList<Message> messages;
    private StorageReference storageRef;

    /***************************************** Constructor ****************************************/
    public MessagesPresenter(FirebaseAuth mAuth, FirebaseFirestore mDb, FirebaseStorage mStorage) {
        this.mAuth = mAuth;
        this.mDb = mDb;
        storageRef = mStorage.getReference();
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
        storageRef.child("images/" + mUser.getUid()).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    mView.setUserInfo(mUser.getDisplayName(), mAuth.getUid(), uri);
                })
                .addOnFailureListener(e -> mView.setUserInfo(mUser.getDisplayName(), mAuth.getUid(), null));
    }

    @Override
    public void copyUid() {
        ClipboardManager clipboard = (ClipboardManager) mView.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Anonymous Messages",
                "https://" + mView.getResources().getString(R.string.website) + "?uid=" + mAuth.getUid());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(mView, "UID Copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateProfilePicture(Uri image_uri) {
        Log.d(TAG, "updateProfilePicture: " + image_uri.toString());

        //Uploading the File
        Log.d(TAG, "updateProfilePicture: " + image_uri.getLastPathSegment());
        StorageReference riversRef = storageRef.child("images/" + mUser.getUid());
        UploadTask uploadTask = riversRef.putFile(image_uri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageRef.child("images/" + mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    mView.setProfilePicture(uri);
                }
            });
        })
                .addOnFailureListener(exception -> Toast.makeText(mView, "Couldn't Update Profile Picture", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void initRecyclerView() {
        mDb.collection("messages")
                .whereEqualTo("uid", mAuth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    messages = new ArrayList<>();
                    if (task.isSuccessful()) {
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
        mDb.collection("messages")
                .whereEqualTo("uid", mAuth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    messages = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Message message = document.toObject(Message.class);
                            message.setId(document.getId());
                            messages.add(message);
                        }

                        mView.updateRecyclerView(messages);

                        if (messages.size() == 0)
                            mView.showNoMessages();
                        else mView.showMessages();
                    } else {
                        mView.updateRecyclerView(messages);
                        mView.showNoNetwork();
                    }
                });
    }

    @Override
    public void deleteMessage(int position) {
        mDb.collection("messages").document(messages.get(position).getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    messages.remove(position);

                    mView.updateRecyclerView(messages);

                    if (messages == null)
                        mView.showNoNetwork();
                    else if (messages.size() == 0)
                        mView.showNoMessages();
                    else mView.showMessages();
                })
                .addOnFailureListener(e -> Toast.makeText(mView, "Couldn't delete message.", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void likeMessage(Message message, boolean liked) {
        Map<String, Object> update = new HashMap<>();
        Log.d(TAG, "likeMessage: " + liked);
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
        LoginManager.getInstance().logOut();
    }
}
