package com.contentstack.persistence;

import org.json.JSONArray;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static io.realm.Realm.getDefaultInstance;


public class RealmStore implements SyncPersistable {

    private Realm realmInstance;

    private String TAG = RealmStore.class.getSimpleName();

    public RealmStore(Realm realmInstance) {
        this.realmInstance = realmInstance;
    }

    public Realm getRealmInstance() {
        return this.realmInstance;
    }


    @Override
    public RealmObject findOrCreate(Class<? extends RealmObject> className, String uid,  JSONArray jsonObjectString) {

        try {

            beginWriteTransaction();
            realmInstance.createOrUpdateAllFromJson(className, jsonObjectString);
            commitWriteTransaction();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            closeTransaction();
        }

        return  realmInstance.where(className).equalTo("uid",uid).findFirst();

    }

    @Override
    public RealmResults<? extends RealmObject> find(Class<? extends RealmObject> className) {
        return realmInstance.where(className).findAll();
    }


    public RealmResults getTable(Class<? extends RealmObject> className){

        RealmResults<? extends RealmObject> schemaTable = realmInstance.where(className).findAll();
        return schemaTable;
    }


    @Override
    public void deleteRow(final Class<? extends RealmObject> className, final String uid){

        realmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (className != null){
                    String primaryKEY = realm.getSchema().get(className.getSimpleName()).getPrimaryKey();
                    RealmResults<? extends RealmObject> rowResult = realm.where(className).equalTo(primaryKEY, uid).findAll();
                    rowResult.deleteAllFromRealm();
                    closeTransaction();
                }
            }
        });

    }


    @Override
    public void deleteTable(Class<? extends RealmObject> className) {

        if (className!=null) {
            realmInstance.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                }
            });
        }
    }


    @Override
    public void beginWriteTransaction() {
        getDefaultInstance();
        realmInstance.beginTransaction();
    }


    @Override
    public void commitWriteTransaction() {
        realmInstance.commitTransaction();
    }


    @Override
    public void closeTransaction() {
        if (!realmInstance.isClosed()){
            realmInstance.close();
        }
    }



}
