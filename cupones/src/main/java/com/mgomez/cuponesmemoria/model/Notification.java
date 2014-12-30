package com.mgomez.cuponesmemoria.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Created by mgomezacid on 20-05-14.
 */
public class Notification implements Parcelable {

    private long id;
    private String title;
    private String message;
    private String init_date;
    private String end_date;
    private int proximity_trigger_range;
    private String access_level;
    private boolean aggregate;
    private boolean read;
    private long received_date;
    private int days;


    public Notification(long id, String title, String message, int proximity_trigger_range, String init_date, String end_date, String access_level){
        this. id = id;
        this.title = title;
        this.message = message;
        this.proximity_trigger_range = proximity_trigger_range;
        this.init_date = init_date;
        this.end_date = end_date;
        this.access_level = access_level;
        this.read = false;
        this.aggregate = false;
        this.received_date = 0;
    }

    public Notification(long id, String title, String message, int proximity_trigger_range, String init_date, String end_date, String access_level, boolean aggregate){
        this. id = id;
        this.title = title;
        this.message = message;
        this.proximity_trigger_range = proximity_trigger_range;
        this.init_date = init_date;
        this.end_date = end_date;
        this.access_level = access_level;
        this.read = false;
        this.aggregate = aggregate;
        this.received_date = 0;
    }

    public Notification(long id, String title, String message, int proximity_trigger_range, String init_date, String end_date, String access_level, boolean read, boolean aggregate, long received_date){
        this. id = id;
        this.title = title;
        this.message = message;
        this.proximity_trigger_range = proximity_trigger_range;
        this.init_date = init_date;
        this.end_date = end_date;
        this.access_level = access_level;
        this.read = read;
        this.aggregate = aggregate;
        this.received_date = received_date;
        generateDay();
    }

    public Notification(Parcel p){
        this.id = p.readInt();
        this.title = p.readString();
        this.message = p.readString();
        this.proximity_trigger_range = p.readInt();
        this.init_date = p.readString();
        this.end_date = p.readString();
        this.access_level = p.readString();
        this.aggregate = false;
        this.read = false;
    }

    private void generateDay() {
        final DateTime start = new DateTime().withTimeAtStartOfDay();
        final DateTime end = new DateTime(getReceived_date());

        this.days = Days.daysBetween(end, start).getDays();
    }

    public long getReceived_date() {
        return received_date;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isAggregate() {
        return aggregate;
    }

    public void setAggregate(boolean visible) {
        this.aggregate = visible;
    }

    public String getAccess_level() {
        return access_level;
    }

    public void setAccess_level(String access_level) {
        this.access_level = access_level;
    }

    public void setReceived_date(long received_date) {
        this.received_date = received_date;
    }

    public int getProximity_trigger_range() {
        return proximity_trigger_range;
    }

    public void setProximity_trigger_range(int proximity_trigger_range) {
        this.proximity_trigger_range = proximity_trigger_range;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeInt(proximity_trigger_range);
        dest.writeString(init_date);
        dest.writeString(end_date);
        dest.writeString(access_level);
    }
}
