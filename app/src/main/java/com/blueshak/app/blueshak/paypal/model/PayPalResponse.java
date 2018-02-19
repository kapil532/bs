package com.blueshak.app.blueshak.paypal.model;

/**
 * Created by lsingh013 on 20/02/18.
 */

public class PayPalResponse {
    private String id;

    private String state;

    private String create_time;

    private String intent;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public String getCreate_time ()
    {
        return create_time;
    }

    public void setCreate_time (String create_time)
    {
        this.create_time = create_time;
    }

    public String getIntent ()
    {
        return intent;
    }

    public void setIntent (String intent)
    {
        this.intent = intent;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", state = "+state+", create_time = "+create_time+", intent = "+intent+"]";
    }
}
