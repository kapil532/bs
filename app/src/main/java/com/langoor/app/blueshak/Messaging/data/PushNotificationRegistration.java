package com.langoor.app.blueshak.Messaging.data;


public class PushNotificationRegistration {

    String deviceType;
    String registrationToken;
    String hardwareId;

    PushNotificationRegistration() {
        // for gson
    }

    public PushNotificationRegistration(String deviceId, String gcmToken) {
        this.hardwareId = deviceId;
        this.registrationToken = gcmToken;
        this.deviceType = "android";
    }
}
