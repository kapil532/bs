package com.blueshak.app.blueshak.seller.model;

/**
 * Created by lsingh013 on 25/05/18.
 */

public class SellerLikeModel {

    private String success;

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [success = "+success+"]";
    }
}
