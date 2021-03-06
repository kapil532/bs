package com.blueshak.app.blueshak.seller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lal on 18/04/18.
 */

public class Image implements Serializable {
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("id")
    @Expose
    private String id;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
