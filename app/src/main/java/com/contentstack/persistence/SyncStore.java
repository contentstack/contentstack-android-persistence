package com.contentstack.persistence;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SyncStore extends RealmObject {

    @PrimaryKey
    private String uniqueId;
    private String sync_token;
    private String pagination_token;

    public SyncStore() {
    }

    public SyncStore(String uniqueId, String sync_token, String pagination_token) {
        this.uniqueId = uniqueId;
        this.sync_token = sync_token;
        this.pagination_token = pagination_token;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSync_token() {
        return sync_token;
    }

    public void setSync_token(String sync_token) {
        this.sync_token = sync_token;
    }

    public String getPagination_token() {
        return pagination_token;
    }

    public void setPagination_token(String pagination_token) {
        this.pagination_token = pagination_token;
    }
}
