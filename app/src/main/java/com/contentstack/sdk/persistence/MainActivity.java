package com.contentstack.sdk.persistence;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;
import com.contentstack.sdk.Config;
import com.contentstack.sdk.Contentstack;
import com.contentstack.sdk.Stack;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkCallingOrSelfPermission("com.contentstack.sdk.permission.MAIN_ACTIVITY_ACCESS")
            != PackageManager.PERMISSION_GRANTED){
            finish();
            return;
        }

        Realm.init(getApplicationContext());
        loadTheSDK();
    }


    private void loadTheSDK() {
        try {
            Config config = new Config();
            config.setHost("*************");
            Stack stack = Contentstack.stack(getApplicationContext(),
                    "*************", "*************",
                    "*************", config);


            Realm realmInstance = Realm.getDefaultInstance();
            RealmStore realmStore = new RealmStore(realmInstance);
            SyncManager manager = new SyncManager(realmStore, stack);
            manager.stackRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}