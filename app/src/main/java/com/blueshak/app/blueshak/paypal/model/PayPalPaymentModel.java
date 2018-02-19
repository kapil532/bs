package com.blueshak.app.blueshak.paypal.model;

/**
 * Created by lsingh013 on 20/02/18.
 */

public class PayPalPaymentModel {
    private PayPalResponse response;

    private String response_type;

    private PayPalResponseClient client;

    public PayPalResponse getResponse ()
    {
        return response;
    }

    public void setResponse (PayPalResponse response)
    {
        this.response = response;
    }

    public String getResponse_type ()
    {
        return response_type;
    }

    public void setResponse_type (String response_type)
    {
        this.response_type = response_type;
    }

    public PayPalResponseClient getClient ()
    {
        return client;
    }

    public void setClient (PayPalResponseClient client)
    {
        this.client = client;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", response_type = "+response_type+", client = "+client+"]";
    }
}
