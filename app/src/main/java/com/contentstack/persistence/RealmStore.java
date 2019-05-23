
/**
 * MIT License
 * Copyright (c) 2012 - 2019 Contentstack
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.contentstack.persistence;
import android.util.Log;
import org.json.JSONArray;
import java.util.Objects;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import static io.realm.Realm.getDefaultInstance;


public class RealmStore implements SyncPersistable {

    private String TAG = RealmStore.class.getSimpleName();
    private Realm realmInstance;
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
            Log.i(TAG, className+" created with the uid "+uid+" and Json Object reponse is: "+jsonObjectString);
        } catch (Exception e) {
            throw new IllegalStateException(e.getLocalizedMessage());
        } finally {
            closeTransaction();
            Log.i(TAG, "Transaction closed for: "+className);
        }

        return  realmInstance.where(className).equalTo("uid",uid).findFirst();
    }


    @Override
    public RealmResults<? extends RealmObject> find(Class<? extends RealmObject> className) {
        Log.i(TAG, "find all by class name: "+className);
        return realmInstance.where(className).findAll();
    }


    public RealmResults getTable(Class<? extends RealmObject> className){
        Log.i(TAG, "Get Table : "+className);
        return realmInstance.where(className).findAll();
    }



    @Override
    public void deleteRow(final Class<? extends RealmObject> className, final String uid){

        realmInstance.executeTransaction(realm -> {
            if (className != null){
                String primaryKEY = Objects.requireNonNull(realm.getSchema().get(className.getSimpleName())).getPrimaryKey();
                RealmResults<? extends RealmObject> rowResult = realm.where(className).equalTo(primaryKEY, uid).findAll();
                rowResult.deleteAllFromRealm();
                closeTransaction();
            }
        });

    }


    @Override
    public void deleteTable(Class<? extends RealmObject> className) {
        if (className!=null){
            Log.i(TAG, "Deleted All Content From "+className);
            realmInstance.executeTransaction(realm -> realm.deleteAll());
        }
    }


    @Override
    public void beginWriteTransaction() {
        getDefaultInstance();
        Log.i(TAG, "Transaction Is In Execution");
        realmInstance.beginTransaction();
    }


    @Override
    public void commitWriteTransaction() {
        Log.i(TAG, "Commit Write Transaction");
        realmInstance.commitTransaction();
    }


    @Override
    public void closeTransaction() {
        Log.i(TAG, "Close Transaction");
        if (!realmInstance.isClosed()){
            realmInstance.close();
        }
    }



}
