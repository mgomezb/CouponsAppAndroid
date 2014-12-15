package com.mgomez.cuponesmemoria.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mgomez.cuponesmemoria.Constants;

/**
 * Created by mgomezacid on 06-05-14.
 */

public class Coupon implements Parcelable{

    public static enum Types {
        PUBLIC("public"), PRIVATE("private");

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
    private String init_date;
    private String end_date;
    private String access_level;
    private String legal;
    private long store_category_id;
    private long interest_point_id;
    private int proximity_trigger_range;
    private String image;
    private String code;
    private int[] notification_beacon_ids;
    private boolean claimed;
    private boolean claimable;
    private String use_instructions;
    private int stock;

    public Coupon(long id, String title, String message, String init_date, String end_date, String access_level, String legal, long store_category_id, long interest_point_id, int proximity_trigger_range, String image, String code, int[] notification_beacon_ids, boolean claimable, String use_instructions, int stock){
        this.id = id;
        this.title = title;
        this.message = message;
        this.init_date = init_date;
        this.end_date = end_date;
        this.access_level = access_level;
        this.legal = legal;
        this.store_category_id = store_category_id;
        this.interest_point_id = interest_point_id;
        this.proximity_trigger_range = proximity_trigger_range;
        this.image = image;
        this.code = code;
        this.notification_beacon_ids = notification_beacon_ids;
        this.claimable = claimable;
        this.use_instructions = use_instructions;
        this.stock = stock;
    }

    public Coupon(long id, String title, String message, String init_date, String end_date, String access_level, String legal, long store_category_id, long interest_point_id, int proximity_trigger_range, String image, String code, boolean claimed, boolean claimable, String use_instructions, int stock){
        this.id = id;
        this.title = title;
        this.message = message;
        this.init_date = init_date;
        this.end_date = end_date;
        this.access_level = access_level;
        this.legal = legal;
        this.store_category_id = store_category_id;
        this.interest_point_id = interest_point_id;
        this.proximity_trigger_range = proximity_trigger_range;
        this.image = image;
        this.code = code;
        this.claimed = claimed;
        this.claimable = claimable;
        this.use_instructions = use_instructions;
        this.stock = stock;
    }

    public Coupon(Parcel p){
        this.id = p.readLong();
        this.title = p.readString();
        this.message = p.readString();
        this.init_date = p.readString();
        this.end_date = p.readString();
        this.access_level = p.readString();
        this.legal = p.readString();
        this.store_category_id = p.readLong();
        this.interest_point_id = p.readLong();
        this.proximity_trigger_range = p.readInt();
        this.image = p.readString();
        this.code = p.readString();
        this.notification_beacon_ids = p.createIntArray();
        this.claimable = p.readByte() != 0;
        this.use_instructions = p.readString();
        this.stock = p.readInt();
    }

    public String getUrlImage(){
        return Constants.SERVER+getImage();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int[] getNotification_beacon_ids() {
        return notification_beacon_ids;
    }

    public void setNotification_beacon_ids(int[] notification_beacon_ids) {
        this.notification_beacon_ids = notification_beacon_ids;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProximity_trigger_range() {
        return proximity_trigger_range;
    }

    public boolean isClaimable() {
        return claimable;
    }

    public void setProximity_trigger_range(int proximity_trigger_range) {
        this.proximity_trigger_range = proximity_trigger_range;
    }

    public String getUse_instructions() {
        return use_instructions;
    }

    public long getInterest_point_id() {
        return interest_point_id;
    }

    public void setInterest_point_id(long interest_point_id) {
        this.interest_point_id = interest_point_id;
    }

    public long getStore_category_id() {
        return store_category_id;
    }

    public void setStore_category_id(long store_category_id) {
        this.store_category_id = store_category_id;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getAccess_level() {
        return access_level;
    }

    public void setAccess_level(String access_level) {
        this.access_level = access_level;
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

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean visible) {
        this.claimed = visible;
    }
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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
        dest.writeString(init_date);
        dest.writeString(end_date);
        dest.writeString(access_level);
        dest.writeString(legal);
        dest.writeLong(store_category_id);
        dest.writeLong(interest_point_id);
        dest.writeInt(proximity_trigger_range);
        dest.writeString(image);
        dest.writeString(code);
        dest.writeIntArray(notification_beacon_ids);
        dest.writeByte((byte) (this.claimable ? 1 : 0));
        dest.writeString(use_instructions);
        dest.writeInt(stock);
    }

    public static final Parcelable.Creator<Coupon> CREATOR = new Parcelable.Creator<Coupon>() {
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };
}
