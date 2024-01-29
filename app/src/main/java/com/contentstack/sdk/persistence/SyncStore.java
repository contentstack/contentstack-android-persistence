package com.contentstack.sdk.persistence;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SyncStore extends RealmObject {

    @PrimaryKey
    private String uniqueId;
    private String syncToken;
    private String paginationToken;

    public SyncStore() {
    }

    public SyncStore(String uniqueId, String syncToken, String paginationToken) {
        this.uniqueId = uniqueId;
        this.syncToken = syncToken;
        this.paginationToken = paginationToken;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSyncToken() {
        return syncToken;
    }

    public void setSyncToken(String sync_token) {
        this.syncToken = sync_token;
    }

    public String getPaginationToken() {
        return paginationToken;
    }

    public void setPaginationToken(String pagination_token) {
        this.paginationToken = pagination_token;
    }
}
