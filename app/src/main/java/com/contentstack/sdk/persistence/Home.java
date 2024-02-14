package com.contentstack.sdk.persistence;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmField;

// This is only for testing support
@RealmClass(name = "a")
public class Home extends RealmObject {

    @PrimaryKey
    @RealmField(name = "uid")
    private String uid;
    @RealmField(name = "title")
    private String mTitle;
}


