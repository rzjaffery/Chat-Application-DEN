package com.rzjaffery.chatapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rzjaffery.chatapplication.R;
import com.rzjaffery.chatapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.VH> {

    public interface OnUserClick { void onClick(User u); }

    private final OnUserClick onUserClick;
    private final List<User> data = new ArrayList<>();

    public UsersAdapter(OnUserClick onUserClick) { this.onUserClick = onUserClick; }

    public void submit(List<User> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_user, p, false);
        return new VH(view);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        User u = data.get(pos);
        h.name.setText(u.name != null ? u.name : "(no name)");
        h.email.setText(u.email != null ? u.email : "");
        h.itemView.setOnClickListener(v -> onUserClick.onClick(u));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, email;
        VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            email = itemView.findViewById(R.id.tv_email);
        }
    }
}
