package com.rzjaffery.chatapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rzjaffery.chatapplication.model.User;
import com.rzjaffery.chatapplication.repository.ChatRepository;

import java.util.List;

public class ChatListViewModel extends ViewModel {
    private final ChatRepository repo = new ChatRepository();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<User>> getUsers() { return users; }
    public LiveData<String> getError() { return error; }

    public void loadUsers() {
        repo.getAllUsers(users::setValue, error::setValue);
    }

    public void setOnline(boolean online) { repo.setOnline(online); }
}
