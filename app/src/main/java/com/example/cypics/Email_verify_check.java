package com.example.cypics;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Email_verify_check {
    public static void addAuthStateListener() {
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    // User is signed in and email is verified, send push notification
                    sendNotification();
                }
            }
        });
    }

    private static void sendNotification() {
        // Code to send push notification
        // You can use Firebase Cloud Messaging (FCM) to send notifications to your app
        // Here's an example code to send a notification using FCM:
        /*///FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(FCM_SENDER_ID + "@fcm.googleapis.com")
                .setMessageId(Integer.toString(messageId))
                .addData("title", "My Title")
                .addData("body", "My notification message.")
                .setToken(token)
                .build());*/
}}
