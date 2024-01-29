package com.contentstack.sdk.persistence;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.contentstack.sdk.Contentstack;
import com.contentstack.sdk.Stack;

import io.realm.Realm;

class TestApp extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTheSDK();
    }

    private void loadTheSDK() {
        try {
            Stack stack = Contentstack.stack(getApplicationContext(),
                    "********", "*********",
                    "********");

            Realm realmInstance = Realm.getDefaultInstance();
            RealmStore realmStore = new RealmStore(realmInstance);
            SyncManager manager = new SyncManager(realmStore, stack);
            manager.stackRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
