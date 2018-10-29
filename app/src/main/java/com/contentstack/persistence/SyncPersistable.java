package com.contentstack.persistence;

import org.json.JSONArray;

import io.realm.RealmObject;
import io.realm.RealmResults;

public interface SyncPersistable {

    void beginWriteTransaction();

    void commitWriteTransaction();

    void closeTransaction();

    RealmObject findOrCreate(Class<? extends RealmObject> className, String uid, JSONArray jsonObjectString);

    RealmResults<? extends RealmObject> find(Class<? extends RealmObject> className);

    void deleteRow(Class<? extends RealmObject> className, String fieldName);

    void deleteTable(Class<? extends RealmObject> className);
}
