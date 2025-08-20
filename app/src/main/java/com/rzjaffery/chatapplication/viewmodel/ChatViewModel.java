package com.rzjaffery.chatapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.rzjaffery.chatapplication.model.Message;
import com.rzjaffery.chatapplication.repository.ChatRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private final ChatRepository repo = new ChatRepository();
    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>(new ArrayList<>());
    private ChildEventListener listener;
    private String withUid;

    public LiveData<List<Message>> getMessages() { return messages; }

    public void start(String otherUid) {
        this.withUid = otherUid;
        listener = repo.listenMessages(otherUid, msg -> {
            List<Message> current = messages.getValue();
            if (current == null) current = new ArrayList<>();
            current.add(msg);
            messages.postValue(current);
        });
    }

    public void stop() {
        if (withUid != null && listener != null) {
            repo.stopListening(withUid, listener);
            listener = null;
        }
    }

    public void send(String toUid, String text) {
        repo.sendMessage(toUid, text, ok -> { /* could expose a status LiveData */});
    }
}
