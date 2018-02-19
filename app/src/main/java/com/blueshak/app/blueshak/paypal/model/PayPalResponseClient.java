package com.blueshak.app.blueshak.paypal.model;

/**
 * Created by lsingh013 on 20/02/18.
 */

public class PayPalResponseClient {
    private String platform;

    private String environment;

    private String product_name;

    private String paypal_sdk_version;

    public String getPlatform ()
    {
        return platform;
    }

    public void setPlatform (String platform)
    {
        this.platform = platform;
    }

    public String getEnvironment ()
    {
        return environment;
    }

    public void setEnvironment (String environment)
    {
        this.environment = environment;
    }

    public String getProduct_name ()
    {
        return product_name;
    }

    public void setProduct_name (String product_name)
    {
        this.product_name = product_name;
    }

    public String getPaypal_sdk_version ()
    {
        return paypal_sdk_version;
    }

    public void setPaypal_sdk_version (String paypal_sdk_version)
    {
        this.paypal_sdk_version = paypal_sdk_version;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [platform = "+platform+", environment = "+environment+", product_name = "+product_name+", paypal_sdk_version = "+paypal_sdk_version+"]";
    }
}
