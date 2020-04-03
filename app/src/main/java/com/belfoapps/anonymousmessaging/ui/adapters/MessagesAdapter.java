package com.belfoapps.anonymousmessaging.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.pojo.Message;
import com.belfoapps.anonymousmessaging.presenters.MessagesPresenter;
import com.belfoapps.anonymousmessaging.ui.views.activities.MessagesActivity;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    /*************************************** Declarations *****************************************/
    private MessagesPresenter mPresenter;
    private ArrayList<Message> mMessages;
    private MessagesActivity mView;

    /*************************************** Constructor ******************************************/
    public MessagesAdapter(ArrayList<Message> mMessages, MessagesActivity mView, MessagesPresenter mPresenter) {
        this.mMessages = mMessages;
        this.mView = mView;
        this.mPresenter = mPresenter;
    }

    /*************************************** Methods **********************************************/
    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_recyclerview_item, parent, false);

        return new MessagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
        holder.message.setText(mMessages.get(position).getMessage());
        holder.delete.setOnClickListener(v -> mPresenter.deleteMessage(mMessages.get(position)));
        //TODO: set Like src
        holder.like.setOnClickListener(v -> {
            if (!mMessages.get(position).isLiked())
                Toast.makeText(mView, "Liked", Toast.LENGTH_SHORT).show();
            else Toast.makeText(mView, "Not Liked", Toast.LENGTH_SHORT).show();

            mPresenter.likeMessage(mMessages.get(position), !mMessages.get(position).isLiked());
        });
    }

    @Override
    public int getItemCount() {
        if (mMessages == null) return 0;
        else return mMessages.size();
    }

    public void clearAll() {
        if (mMessages != null) mMessages.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Message> messages) {
        mMessages = messages;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        ImageButton like;
        ImageButton delete;

        ViewHolder(View v) {
            super(v);
            message = v.findViewById(R.id.message);
            like = v.findViewById(R.id.like);
            delete = v.findViewById(R.id.delete);
        }
    }

}
