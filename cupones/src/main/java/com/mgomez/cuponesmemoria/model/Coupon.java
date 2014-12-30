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
    private long store_id;
    private long category;
    private int proximity_trigger_range;
    private String image_url;
    private String code;
    private boolean claimed;
    private String store_name;
    private int stock;
    private String use_instructions;

    public Coupon(long id, String title, String message, String init_date, String end_date, String access_level, String legal, int proximity_trigger_range, String image_url, String code, int stock, long store_id, long category, String store_name, String use_instructions){
        this.id = id;
        this.title = title;
        this.message = message;
        this.init_date = init_date;
        this.end_date = end_date;
        this.access_level = access_level;
        this.legal = legal;
        this.proximity_trigger_range = proximity_trigger_range;
        this.code = code;
        this.stock = stock;
        this.image_url = image_url;
        this.category = category;
        this.store_name = store_name;
        this.store_id = store_id;
        this.use_instructions = use_instructions;
    }

    public Coupon(long id, String title, String message, String init_date, String end_date, String access_level, String legal, int proximity_trigger_range, String image_url, String code, int stock, long store_id, long category, String store_name, boolean claimed, String use_instructions){
        this.id = id;
        this.title = title;
        this.message = message;
        this.init_date = init_date;
        this.end_date = end_date;
        this.access_level = access_level;
        this.legal = legal;
        this.proximity_trigger_range = proximity_trigger_range;
        this.code = code;
        this.claimed = claimed;
        this.stock = stock;
        this.image_url = image_url;
        this.category = category;
        this.store_name = store_name;
        this.store_id = store_id;
        this.use_instructions = use_instructions;
    }

    public Coupon(Parcel p){
        this.id = p.readLong();
        this.title = p.readString();
        this.message = p.readString();
        this.init_date = p.readString();
        this.end_date = p.readString();
        this.access_level = p.readString();
        this.legal = p.readString();
        this.proximity_trigger_range = p.readInt();
        this.code = p.readString();
        this.stock = p.readInt();
        this.image_url = p.readString();
        this.category = p.readLong();
        this.store_name = p.readString();
        this.store_id = p.readLong();
        this.use_instructions = p.readString();
    }

    public String getUrlImage(){
        return Constants.SERVER+getImage_url();
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getProximity_trigger_range() {
        return proximity_trigger_range;
    }


    public void setProximity_trigger_range(int proximity_trigger_range) {
        this.proximity_trigger_range = proximity_trigger_range;
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

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }


    public String getUse_instructions() {
        return use_instructions;
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
        dest.writeInt(proximity_trigger_range);
        dest.writeString(code);
        dest.writeInt(stock);
        dest.writeString(image_url);
        dest.writeLong(category);
        dest.writeString(store_name);
        dest.writeLong(store_id);
        dest.writeString(use_instructions);
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
