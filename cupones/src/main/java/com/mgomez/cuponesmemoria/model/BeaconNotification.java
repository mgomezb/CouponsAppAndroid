package com.mgomez.cuponesmemoria.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mgomezacid on 13-05-14.
 */
public class BeaconNotification implements Parcelable{

    private long id;
    private int mayor;
    private int minor;
    private String proximity_uuid;
    private int[] notifications_ids;
    private int[] coupons_ids;

    public BeaconNotification(long id, int mayor, int minor, String proximity_uuid, int[] notifications_ids, int[] coupons_ids){
        this.id = id;
        this.mayor = mayor;
        this.minor = minor;
        this.proximity_uuid = proximity_uuid;
        this.notifications_ids = notifications_ids;
        this.coupons_ids = coupons_ids;
    }

    public BeaconNotification(long id, int mayor, int minor, String proximity_uuid){
        this.id = id;
        this.mayor = mayor;
        this.minor = minor;
        this.proximity_uuid = proximity_uuid;
    }

    public BeaconNotification(Parcel parcel){
        this.id = parcel.readLong();
        this.mayor = parcel.readInt();
        this.minor = parcel.readInt();
        this.proximity_uuid = parcel.readString();
        this.notifications_ids = parcel.createIntArray();
        this.coupons_ids = parcel.createIntArray();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMayor() {
        return mayor;
    }

    public void setMayor(int mayor) {
        this.mayor = mayor;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getProximity_uuid() {
        return proximity_uuid;
    }

    public void setProximity_uuid(String proximity_uuid) {
        this.proximity_uuid = proximity_uuid;
    }

    public int[] getNotifications_ids() {
        return notifications_ids;
    }

    public void setNotifications_ids(int[] alerts_ids) {
        this.notifications_ids = notifications_ids;
    }

    public int[] getCoupons_ids() {
        return coupons_ids;
    }

    public void setCoupons_ids(int[] coupons_ids) {
        this.coupons_ids = coupons_ids;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(mayor);
        dest.writeInt(minor);
        dest.writeString(proximity_uuid);
        dest.writeIntArray(notifications_ids);
        dest.writeIntArray(coupons_ids);
    }

    public static final Parcelable.Creator<BeaconNotification> CREATOR = new Parcelable.Creator<BeaconNotification>() {
        public BeaconNotification createFromParcel(Parcel in) {
            return new BeaconNotification(in);
        }

        public BeaconNotification[] newArray(int size) {
            return new BeaconNotification[size];
        }
    };
}
