package com.contentstack.sdk.persistence;

import androidx.annotation.NonNull;

import android.util.ArrayMap;
import android.util.Log;

import com.contentstack.sdk.Error;
import com.contentstack.sdk.Stack;
import com.contentstack.sdk.SyncResultCallBack;
import com.contentstack.sdk.SyncStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.annotations.RealmField;


public class SyncManager {

    final private String TAG = SyncManager.class.getSimpleName();
    final private Logger logger = Logger.getLogger(TAG);
    final private Stack stackInstance;
    final private RealmStore realmStoreInstance;
    final private Realm realmInstance;
    private ArrayMap<String, Class> CONTENT_TYPE_CLASS_MAPPER = new ArrayMap<String, Class>();
    private ArrayMap<Class, ArrayMap<String, String>> CLASS_FIELDS_MAPPER = new ArrayMap<Class, ArrayMap<String, String>>();


    /**
     * SyncManager Constructor Accepts below two Realm Store and Stack Instances.
     *
     * @param realmStore    @{@link RealmStore} that accepts realm instance to initialise.
     * @param stackInstance @{@link Stack} takes stackInstance as second parameter.
     */
    public SyncManager(@NonNull RealmStore realmStore, @NonNull Stack stackInstance) {
        this.realmStoreInstance = realmStore;
        this.stackInstance = stackInstance;
        realmInstance = realmStoreInstance.getRealmInstance();
        setClassMapping();
    }


    private void handleError(Error error) {

        if (error.getStatusCode() == 422) {

            if (error.getErrors().containsKey("pagination_token")) {

                deleteToken(null, getPaginationToken()); // Delete pagination token from local DB
                stackRequest();
            } else if (error.getErrors().containsKey("sync_token")) {

                deleteToken(getSyncToken(), null); // Delete sync token from local DB
                stackRequest();
            } else {

                try {
                    throw new Exception();
                } catch (Exception e) {
                    throw new RuntimeException(e.getLocalizedMessage());
                }
            }

        } else if (error.getStatusCode() == 429) {
            int delayMillis = (int) (1000 + Math.random() * 2000);

            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
            stackRequest();
        }
    }

    public void stackRequest() {

        if (getSyncToken() != null) {
            Log.i(TAG, "Call initiated for Sync Token " + getSyncToken());
            stackInstance.syncToken(getSyncToken(), new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack syncStack, Error error) {
                    if (error == null) {
                        Log.i(TAG, syncStack.getJSONResponse().toString());
                        parseResponse(syncStack);
                    } else {
                        handleError(error);
                    }
                }
            });

        } else if (getPaginationToken() != null) {
            Log.i(TAG, "Call initiated for Pagination Token " + getPaginationToken());
            stackInstance.syncPaginationToken(getPaginationToken(), new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack syncStack, Error error) {
                    if (error == null) {
                        logger.log(Level.INFO, syncStack.getJSONResponse().toString());
                        parseResponse(syncStack);
                    } else {
                        handleError(error);
                    }
                }
            });
        } else {
            stackInstance.sync(new SyncResultCallBack() {
                @Override
                public void onCompletion(SyncStack syncStack, Error error) {
                    Log.i(TAG, "Initial Sync Call");
                    if (error == null) {
                        logger.log(Level.INFO, syncStack.getJSONResponse().toString());
                        parseResponse(syncStack);
                    } else {
                        handleError(error);
                    }
                }
            });
        }
    }


    private String getSyncToken() {
        SyncStore syncStore = realmInstance.where(SyncStore.class).findFirst();
        if (syncStore != null) {
            return realmInstance.where(SyncStore.class).findFirst().getSyncToken();
        } else {
            Log.e(TAG, "Sync Token Not Found");
            return null;
        }
    }


    private String getPaginationToken() {
        SyncStore syncStore = realmInstance.where(SyncStore.class).findFirst();
        if (syncStore != null) {
            return realmInstance.where(SyncStore.class).findFirst().getPaginationToken();
        } else {
            Log.e(TAG, "Pagination Token Not Found");
            return null;
        }
    }


    private void parseResponse(SyncStack stackResponse) {
        ArrayList<JSONObject> jsonList = stackResponse.getItems();
        String syncToken = stackResponse.getSyncToken();
        String pagiToken = stackResponse.getPaginationToken();
        if (syncToken != null) {
            persistsToken(syncToken, pagiToken);
        }
        jsonList.forEach(this::handleJSON);
    }


    private void handleJSON(JSONObject item) {

        String contentType = item.optString("content_type_uid");
        boolean containsKEY = CONTENT_TYPE_CLASS_MAPPER.containsKey(contentType);
        if (containsKEY) {
            Class modelClass = CONTENT_TYPE_CLASS_MAPPER.get(contentType);
            if (item.has("data")) {
                String publishType = item.optString("type");
                JSONObject resultObject = item.optJSONObject("data");
                String uid = item.optJSONObject("data").optString("uid");

                if (publishType.equalsIgnoreCase("entry_published") || publishType.equalsIgnoreCase("asset_published")) {

                    JSONArray mapJSON = new JSONArray();
                    mapJSON.put(jsonDECODER(modelClass, resultObject));
                    realmStoreInstance.findOrCreate(modelClass, uid, mapJSON);

                } else if (publishType.equalsIgnoreCase("entry_unpublished") || publishType.equalsIgnoreCase("entry_deleted") || publishType.equalsIgnoreCase("asset_unpublished") || publishType.equalsIgnoreCase("asset_deleted")) {

                    if (item.has("data") && uid != null) {
                        realmStoreInstance.deleteRow(modelClass, uid);
                    }
                } else if (publishType.equalsIgnoreCase("content_type_deleted")) {
                    realmStoreInstance.deleteTable(modelClass);
                }
            }
        }
    }


    private JSONObject jsonDECODER(Class tableClass, JSONObject resultObject) {

        JSONObject resultObj = new JSONObject();
        ArrayMap<String, String> fieldMap = CLASS_FIELDS_MAPPER.get(tableClass);
        Iterator keyValueIterator = null;
        if (fieldMap != null && fieldMap.size() > 0) {
            keyValueIterator = fieldMap.entrySet().iterator();
        }
        while (keyValueIterator.hasNext()) {
            Map.Entry keyValueEntry = (Map.Entry) keyValueIterator.next();
            String keyString = (String) keyValueEntry.getKey();
            if (resultObject.has(keyString)) {
                resultObj = getJsonTypeToParse(tableClass, resultObj, resultObject, keyValueEntry, keyString);
            }
        }
        return resultObj;
    }


    private JSONObject getJsonTypeToParse(Class tableClass, JSONObject reqObj, JSONObject resultObject, Map.Entry keyValueEntry, String keyString) {
        try {
            String valueString = resultObject.optString(keyString);
            String fieldName = (String) keyValueEntry.getValue();
            Field field = tableClass.getDeclaredField(fieldName);

            if (getInstanceOfField(field).equalsIgnoreCase("RealmObject")) {
                Object refVALUE = resultObject.opt(keyString);
                reqObj = caseWhenTableExtendsRealmObject(fieldName, field, reqObj, refVALUE);

            } else if (getInstanceOfField(field).equalsIgnoreCase("RealmList")) {
                JSONObject refCreate = new JSONObject();
                Object refVALUE = resultObject.opt(keyString);
                reqObj = caseWhenTableExtendsRealmList(fieldName, field, reqObj, refCreate, refVALUE);
            } else {
                reqObj.put(fieldName, valueString);
            }

        } catch (JSONException e) {
            e.getLocalizedMessage();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return reqObj;
    }


    private JSONObject caseWhenTableExtendsRealmObject(String fieldName, Field field, JSONObject reqObj, Object refVALUE) throws JSONException {
        JSONObject refCreate = new JSONObject();
        if (refVALUE instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) refVALUE;
            if (jsonArray.length() > 0) {
                Object checkInstanceVar = jsonArray.get(0);
                if (checkInstanceVar instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) checkInstanceVar;
                    jsonObj = jsonDECODER(field.getType(), jsonObj);
                    reqObj.put(fieldName, jsonObj);
                } else {
                    refCreate.put("uid", jsonArray.get(0));
                    reqObj.put(fieldName, refCreate);
                }
            }
        } else if (refVALUE instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) refVALUE;
            reqObj.put(fieldName, jsonDECODER(field.getType(), jsonObject));
        }
        return reqObj;
    }


    private JSONObject caseWhenTableExtendsRealmList(String fieldName, Field field, JSONObject reqObj, JSONObject refCreate, Object refVALUE) throws JSONException {
        ParameterizedType ge = (ParameterizedType) field.getGenericType();
        Class<?> fieldGetType = (Class<?>) ge.getActualTypeArguments()[0];
        if (refVALUE instanceof JSONArray) {
            JSONArray jsonReference = new JSONArray();
            JSONArray jsonArray = (JSONArray) refVALUE;
            for (int i = 0; i < jsonArray.length(); i++) {
                Object refOBJ = jsonArray.get(i);
                if (refOBJ instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) refOBJ;
                    jsonObj = jsonDECODER(fieldGetType, jsonObj);
                    jsonReference.put(jsonObj);
                } else {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("uid", refOBJ);
                    jsonReference.put(jsonObj);
                }
            }
            reqObj.put(fieldName, jsonReference);

        } else if (refVALUE instanceof JSONObject) {
            JSONArray jsonReference = new JSONArray();
            JSONObject jsonObj = (JSONObject) refVALUE;
            jsonObj = jsonDECODER(fieldGetType, jsonObj);
            jsonReference.put(jsonObj);
            reqObj.put(fieldName, jsonReference);

        } else {
            refCreate.put("uid", refVALUE);
            reqObj.put(fieldName, refCreate);
        }

        return reqObj;
    }


    private String getInstanceOfField(Field field) {
        String checkTYPE = field.getType().getSuperclass().getSimpleName();
        String fieldTYPE = null;
        if (checkTYPE.equalsIgnoreCase("RealmObject")) {
            fieldTYPE = "RealmObject";
        } else if (checkTYPE.equalsIgnoreCase("AbstractList")) {
            fieldTYPE = "RealmList";
        } else {
            fieldTYPE = "Object";
        }
        return fieldTYPE;
    }


    private void setClassMapping() {
        Set<Class<? extends RealmModel>> MODEL_CLASSES = realmStoreInstance.getRealmInstance().getConfiguration().getRealmObjectClasses();
        MODEL_CLASSES.forEach(this::reflectionThing);
    }


    private void reflectionThing(Class<? extends RealmModel> MODEL_CLASS) {

        for (Annotation annotation : MODEL_CLASS.getAnnotations()) {
            for (Method method : annotation.annotationType().getDeclaredMethods()) {
                try {
                    Object CONTENT_TYPE_NAME = method.invoke(annotation, (Object[]) null);
                    if (!CONTENT_TYPE_NAME.toString().isEmpty() && !CONTENT_TYPE_NAME.toString().equalsIgnoreCase("NO_POLICY")) {
                        ArrayMap<String, String> fieldList = new ArrayMap<String, String>();
                        for (Field declaredField : MODEL_CLASS.getDeclaredFields()) {
                            if (declaredField.isAnnotationPresent(RealmField.class)) {

                                RealmField ann = declaredField.getAnnotation(RealmField.class);
                                fieldList.put(ann.name(), declaredField.getName());
                            }
                        }
                        CLASS_FIELDS_MAPPER.put(MODEL_CLASS, fieldList);
                        CONTENT_TYPE_CLASS_MAPPER.put(CONTENT_TYPE_NAME.toString(), MODEL_CLASS);
                    } else {

                        // For Group If CONTENT_TYPE_NAME will be null then considered Table will be type of group
                        ArrayMap<String, String> fieldList = new ArrayMap<String, String>();
                        for (Field declaredField : MODEL_CLASS.getDeclaredFields()) {
                            if (declaredField.isAnnotationPresent(RealmField.class)) {

                                RealmField ann = declaredField.getAnnotation(RealmField.class);
                                fieldList.put(ann.name(), declaredField.getName());
                            }
                        }
                        CLASS_FIELDS_MAPPER.put(MODEL_CLASS, fieldList);
                        CONTENT_TYPE_CLASS_MAPPER.put(CONTENT_TYPE_NAME.toString(), MODEL_CLASS);

                    }

                } catch (Exception e) {
                    Log.e("Exception while Reflection :", e.getLocalizedMessage().toString());
                    e.printStackTrace();
                }
            }
        }
    }


    private void persistsToken(String sync_token, String pagination_token) {
        Log.e("tokens :", "sync_token: " + sync_token + " pagination_token: " + pagination_token);
        try {
            realmStoreInstance.beginWriteTransaction();
            realmStoreInstance.getRealmInstance().insertOrUpdate(new SyncStore("token", sync_token, pagination_token));
            realmStoreInstance.commitWriteTransaction();
        } catch (Exception e) {
            Log.e("Persistence Token :", e.getLocalizedMessage().toString());
            e.printStackTrace();
        } finally {
            realmStoreInstance.closeTransaction();
        }
    }

    private void deleteToken(String syncToken, String paginationToken) {
        try {
            realmStoreInstance.beginWriteTransaction();
            if (syncToken != null) {
                realmStoreInstance.getRealmInstance().insertOrUpdate(new SyncStore("token", null, paginationToken));
                Log.i(TAG, "syncToken deleted");
            }
            if (paginationToken != null) {
                realmStoreInstance.getRealmInstance().insertOrUpdate(new SyncStore("token", syncToken, null));
                Log.i(TAG, "paginationToken deleted");
            }
            if (syncToken != null && paginationToken != null) {
                realmStoreInstance.getRealmInstance().insertOrUpdate(new SyncStore("token", null, null));
                Log.i(TAG, "sync and pagination token deleted");
            }
            realmStoreInstance.commitWriteTransaction();
        } catch (Exception e) {
            Log.e("Persistence Token :", e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            realmStoreInstance.closeTransaction();
        }
    }


}
