package com.rzjaffery.chatapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rzjaffery.chatapplication.R;
import com.rzjaffery.chatapplication.model.Message;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;
    private List<Message> messages;
    private String myId;

    public MessageAdapter(List<Message> messages, String myId) {
        this.messages = messages;
        this.myId = myId;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).senderId.equals(myId) ? TYPE_SENT : TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_out, parent, false);
            return new SentHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_in, parent, false);
            return new ReceivedHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message m = messages.get(position);
        String time = new SimpleDateFormat("HH:mm").format(new Date(m.timestamp));
        if (holder instanceof SentHolder) {
            ((SentHolder) holder).msg.setText(m.text + "\n" + time);
        } else {
            ((ReceivedHolder) holder).msg.setText(m.text + "\n" + time);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class SentHolder extends RecyclerView.ViewHolder {
        TextView msg;
        SentHolder(View v) { super(v); msg = v.findViewById(R.id.tv_sent); }
    }

    static class ReceivedHolder extends RecyclerView.ViewHolder {
        TextView msg;
        ReceivedHolder(View v) { super(v); msg = v.findViewById(R.id.tv_received); }
    }
}
