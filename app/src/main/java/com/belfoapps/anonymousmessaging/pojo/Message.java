package com.belfoapps.anonymousmessaging.pojo;

public class Message {
    private String id;
    private String uid;
    private String message;
    private boolean liked;

    public Message() {
    }

    public Message(String id, String uid, String message, boolean liked) {
        this.id = id;
        this.uid = uid;
        this.message = message;
        this.liked = liked;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
