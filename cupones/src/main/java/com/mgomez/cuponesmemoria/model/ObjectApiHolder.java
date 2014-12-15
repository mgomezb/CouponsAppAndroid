package com.mgomez.cuponesmemoria.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mgomezacid on 13-05-14.
 */
public class ObjectApiHolder {

    @SerializedName("coupons")
    private Coupon[] coupons;

    @SerializedName("notification_beacons")
    private BeaconNotification[] beaconNotifications;

    @SerializedName("alerts")
    private Alert[] alerts;

    @SerializedName("version")
    private String version;

    public String getVersion() {
        return version;
    }

    public Coupon[] getCoupons() {
        return coupons;
    }

    public BeaconNotification[] getBeaconNotifications() {
        return beaconNotifications;
    }

    public Alert[] getAlerts() {
        return alerts;
    }
}
