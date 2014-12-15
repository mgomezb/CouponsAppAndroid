package com.mgomez.cuponesmemoria.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.Days;


/**
 * Created by mgomezacid on 13-05-14.
 */
public class Alert extends Notification implements Parcelable {


    private int proximity_trigger_range;
    private String init_date;
    private String end_date;
    private int[] notification_beacon_ids;
    private int days;

    public Alert(long id, String title, String message, int proximity_trigger_range, String init_date, String end_date, int[] notification_beacon_ids){
        super(id, title, message, Types.ALERT.toString());
        this.proximity_trigger_range = proximity_trigger_range;
        this.init_date = init_date;
        this.end_date = end_date;
        this.notification_beacon_ids = notification_beacon_ids;
    }

    public Alert(long id, String title, String message, int proximity_trigger_range, String init_date, String end_date, boolean isAggregate, String type){
        super(id, title, message, type, false, isAggregate, 0);
        this.proximity_trigger_range = proximity_trigger_range;
        this.init_date = init_date;
        this.end_date = end_date;
    }

    public Alert(long id, String title, String message, int proximity_trigger_range, String init_date, String end_date, boolean read, boolean aggregate, String type, long received_date){
        super(id, title, message, type, read, aggregate, received_date);
        this.proximity_trigger_range = proximity_trigger_range;
        this.init_date = init_date;
        this.end_date = end_date;
        generateDay();
    }

    private void generateDay() {
        final DateTime start = new DateTime().withTimeAtStartOfDay();
        final DateTime end = new DateTime(getReceived_date());

        this.days = Days.daysBetween(end, start).getDays();
    }

    public Alert(Parcel p){
        super(p);
        this.proximity_trigger_range = p.readInt();
        this.init_date = p.readString();
        this.end_date = p.readString();
        this.notification_beacon_ids = p.createIntArray();
    }

    public int getDays(){
        return this.days;
    }

    public int getProximity_trigger_range() {
        return proximity_trigger_range;
    }

    public void setProximity_trigger_range(int proximity_trigger_range) {
        this.proximity_trigger_range = proximity_trigger_range;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public int[] getNotification_beacon_ids() {
        return notification_beacon_ids;
    }

    public void setNotification_beacon_ids(int[] notification_beacon_ids) {
        this.notification_beacon_ids = notification_beacon_ids;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(proximity_trigger_range);
        dest.writeString(init_date);
        dest.writeString(end_date);
        dest.writeIntArray(notification_beacon_ids);
    }

    public static final Parcelable.Creator<Alert> CREATOR = new Parcelable.Creator<Alert>() {
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };
}
