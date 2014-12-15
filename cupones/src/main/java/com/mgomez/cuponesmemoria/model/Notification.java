package com.mgomez.cuponesmemoria.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mgomezacid on 20-05-14.
 */
public class Notification implements Parcelable {

    public static enum Types {
        ALERT("alert"), NOTIFICATION("notification");

        private String dbColumnValue;

        Types(String dbColumnValue) {
            this.dbColumnValue = dbColumnValue;
        }

        @Override
        public String toString() {
            return dbColumnValue.toString();
        }
    }

    private long id;
    private String title;
    private String message;
    private boolean aggregate;
    private boolean read;
    private String type;
    private long received_date;


    public Notification(long id, String title, String message, String type){
        this. id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.read = false;
        this.aggregate = false;
        this.received_date = 0;
    }

    public Notification(long id, String title, String message, String type, boolean read, boolean aggregate, long received_date){
        this. id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.read = read;
        this.aggregate = aggregate;
        this.received_date = received_date;
    }

    public Notification(Parcel p){
        this.id = p.readInt();
        this.title = p.readString();
        this.message = p.readString();
        this.type = p.readString();
        this.aggregate = false;
        this.read = false;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAggregate() {
        return aggregate;
    }

    public void setAggregate(boolean visible) {
        this.aggregate = visible;
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
        dest.writeString(type);
    }
}
