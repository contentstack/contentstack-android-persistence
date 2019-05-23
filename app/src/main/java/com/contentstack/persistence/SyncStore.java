package com.contentstack.persistence;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * @Author Shailesh Mishra (shailesh.mishra@contentstack.com)
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * MIT License
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (c) 2012 - 2019 Contentstack
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

public class SyncStore extends RealmObject {

    @PrimaryKey
    private String uniqueId;
    private String sync_token;
    private String pagination_token;

    public SyncStore() { }

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
    public String getPagination_token() { return pagination_token; }
    public void setPagination_token(String pagination_token) { this.pagination_token = pagination_token; }
}
