package com.mgomez.cuponesmemoria.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mgomezacid on 13-05-14.
 */
public class ObjectApiHolder {

    @SerializedName("coupons")
    private Coupon[] coupons;

    @SerializedName("beacons")
    private BeaconNotification[] beaconNotifications;

    @SerializedName("notifications")
    private Notification[] notifications;

    @SerializedName("version")
    private String version;

    @SerializedName("categories")
    private Category[] categories;

    public String getVersion() {
        return version;
    }

    public Coupon[] getCoupons() {
        return coupons;
    }

    public BeaconNotification[] getBeaconNotifications() {
        return beaconNotifications;
    }

    public Notification[] getAlerts() {
        return notifications;
    }

    public Category[] getCategories() {
        return categories;
    }
}
