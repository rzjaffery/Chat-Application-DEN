package com.rzjaffery.chatapplication.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.rzjaffery.chatapplication.repository.ChatRepository;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "New token: " + token);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            new ChatRepository().updateFcmToken(token);
        }
    }

}
