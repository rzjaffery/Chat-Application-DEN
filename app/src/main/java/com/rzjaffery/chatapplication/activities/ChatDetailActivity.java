package com.rzjaffery.chatapplication.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.rzjaffery.chatapplication.R;
import com.rzjaffery.chatapplication.adapter.MessageAdapter;
import com.rzjaffery.chatapplication.model.Message;
import java.util.ArrayList;

public class ChatDetailActivity extends AppCompatActivity {
    private String receiverId, receiverName, senderId;
    private DatabaseReference chatRef;
    private ArrayList<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail_list);

        receiverId = getIntent().getStringExtra("receiverId");
        receiverName = getIntent().getStringExtra("receiverName");
        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView recyclerView = findViewById(R.id.recycler_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(messages, senderId);
        recyclerView.setAdapter(adapter);

        EditText input = findViewById(R.id.et_message);
        Button sendBtn = findViewById(R.id.btn_send);

        chatRef = FirebaseDatabase.getInstance().getReference("Chats");

        sendBtn.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                String key = chatRef.push().getKey();
                Message msg = new Message(senderId, receiverId, text, System.currentTimeMillis());
                chatRef.child(key).setValue(msg);
                input.setText("");
            }
        });

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Message m = ds.getValue(Message.class);
                    if (m != null &&
                            ((m.senderId.equals(senderId) && m.receiverId.equals(receiverId)) ||
                                    (m.senderId.equals(receiverId) && m.receiverId.equals(senderId)))) {
                        messages.add(m);
                    }
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
