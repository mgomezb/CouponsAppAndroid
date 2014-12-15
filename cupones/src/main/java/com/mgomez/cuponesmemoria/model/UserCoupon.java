package com.mgomez.cuponesmemoria.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mgomezacid on 28-05-14.
 */
public class UserCoupon implements Parcelable{

    String names, last_names, rut, email, address, gender;

    public UserCoupon(String names, String last_names, String rut, String email, String address, String gender){
        this.names = names;
        this.last_names = last_names;
        this.rut = rut;
        this.email = email;
        this.address = address;
        this.gender = gender;
    }

    public UserCoupon(Parcel p){
        this.names = p.readString();
        this.last_names = p.readString();
        this.rut = p.readString();
        this.email = p.readString();
        this.address = p.readString();
        this.gender = p.readString();
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLast_names() {
        return last_names;
    }

    public void setLast_names(String last_names) {
        this.last_names = last_names;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNameComplete(){
        return names+" "+last_names;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getNames());
        dest.writeString(getLast_names());
        dest.writeString(getRut());
        dest.writeString(getEmail());
        dest.writeString(getAddress());
        dest.writeString(getGender());
    }

    public static final Parcelable.Creator<UserCoupon> CREATOR = new Parcelable.Creator<UserCoupon>() {
        public UserCoupon createFromParcel(Parcel in) {
            return new UserCoupon(in);
        }

        public UserCoupon[] newArray(int size) {
            return new UserCoupon[size];
        }
    };
}
