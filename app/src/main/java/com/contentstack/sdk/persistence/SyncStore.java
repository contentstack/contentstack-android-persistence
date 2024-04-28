package com.contentstack.sdk.persistence;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SyncStore extends RealmObject {

    public SyncStore(String uniqueId, String syncToken, String paginationToken, String seqToken) {
        this.uniqueId = uniqueId;
        this.syncToken = syncToken;
        this.paginationToken = paginationToken;
        this.seqToken = seqToken;
    }

    public SyncStore() {
    }

    @PrimaryKey
    private String uniqueId;
    private String syncToken;
    private String paginationToken;
    private String seqToken;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSyncToken() {
        return syncToken;
    }

    public void setSyncToken(String syncToken) {
        this.syncToken = syncToken;
    }

    public String getPaginationToken() {
        return paginationToken;
    }

    public void setPaginationToken(String paginationToken) {
        this.paginationToken = paginationToken;
    }

    public String getSeqToken() {
        return seqToken;
    }

    public void setSeqToken(String seqToken) {
        this.seqToken = seqToken;
    }


}