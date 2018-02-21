package com.blueshak.app.blueshak.paypal.model;

/**
 * Created by lal babu on 20/02/18.
 */

public class PayPaymentResponseModel {

    private String status_code;

    private String status;

    public String getStatus_code ()
    {
        return status_code;
    }

    public void setStatus_code (String status_code)
    {
        this.status_code = status_code;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status_code = "+status_code+", status = "+status+"]";
    }
}
