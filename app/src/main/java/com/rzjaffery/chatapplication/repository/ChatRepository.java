package com.rzjaffery.chatapplication.repository;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.rzjaffery.chatapplication.model.Message;
import com.rzjaffery.chatapplication.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ChatRepository {

    private final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
    private final DatabaseReference convRef = FirebaseDatabase.getInstance().getReference("Conversations");

    private String currentUid() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static String conversationIdFor(String uidA, String uidB) {
        List<String> pair = new ArrayList<>();
        pair.add(uidA);
        pair.add(uidB);
        Collections.sort(pair);
        return pair.get(0) + "_" + pair.get(1);
    }

    // --- Users ---

    public void getAllUsers(Consumer<List<User>> onUsers, Consumer<String> onError) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> list = new ArrayList<>();
                for (DataSnapshot s: snapshot.getChildren()) {
                    User u = s.getValue(User.class);
                    if (u != null && u.uid != null && !u.uid.equals(currentUid())) {
                        list.add(u);
                    }
                }
                onUsers.accept(list);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {
                onError.accept(error.getMessage());
            }
        });
    }

    public void setOnline(boolean online) {
        String uid = currentUid();
        if (uid == null) return;
        usersRef.child(uid).child("online").setValue(online);
    }

    public void updateFcmToken(String token) {
        String uid = currentUid();
        if (uid == null) return;
        usersRef.child(uid).child("fcmToken").setValue(token);
    }

    // --- Messages ---

    public ChildEventListener listenMessages(String otherUid, Consumer<Message> onAdded) {
        String uid = currentUid();
        if (uid == null) return null;

        String convId = conversationIdFor(uid, otherUid);
        DatabaseReference ref = convRef.child(convId);

        ChildEventListener listener = new ChildEventListener() {
            @Override public void onChildAdded(@NonNull DataSnapshot ds, String prevKey) {
                Message m = ds.getValue(Message.class);
                if (m != null) onAdded.accept(m);
            }
            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        };
        ref.addChildEventListener(listener);
        return listener;
    }

    public void stopListening(String otherUid, ChildEventListener listener) {
        String uid = currentUid();
        if (uid == null || listener == null) return;
        String convId = conversationIdFor(uid, otherUid);
        convRef.child(convId).removeEventListener(listener);
    }

    public void sendMessage(String toUid, String text, Consumer<Boolean> onDone) {
        String fromUid = currentUid();
        if (fromUid == null) {
            onDone.accept(false);
            return;
        }
        String convId = conversationIdFor(fromUid, toUid);
        DatabaseReference pushRef = convRef.child(convId).push();
        String id = pushRef.getKey();

        Message m = new Message(id, fromUid, toUid, text, System.currentTimeMillis());

        pushRef.setValue(m).addOnCompleteListener(t -> onDone.accept(t.isSuccessful()));
    }

}
