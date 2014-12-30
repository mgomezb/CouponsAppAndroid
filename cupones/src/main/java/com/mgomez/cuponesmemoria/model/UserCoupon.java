package com.mgomez.cuponesmemoria.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mgomezacid on 28-05-14.
 */
public class UserCoupon implements Parcelable{

    String name;
    String last_name;
    String rut;
    String email;
    String address;
    String gender;
    String password;
    String authentication_token;
    int id;


    public UserCoupon(String name, String last_name, String rut, String email, String address, String gender){
        this.name = name;
        this.last_name = last_name;
        this.rut = rut;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.password = "manolete";
    }

    public UserCoupon(String name, String last_name, String rut, String email, String address, String gender, String token){
        this.name = name;
        this.last_name = last_name;
        this.rut = rut;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.authentication_token = token;
    }

    public UserCoupon(Parcel p){
        this.name = p.readString();
        this.last_name = p.readString();
        this.rut = p.readString();
        this.email = p.readString();
        this.address = p.readString();
        this.gender = p.readString();
        this.authentication_token = p.readString();
        this.id = p.readInt();
    }

    public String getNames() {
        return name;
    }

    public void setNames(String names) {
        this.name = names;
    }

    public String getLast_names() {
        return last_name;
    }

    public void setLast_names(String last_names) {
        this.last_name = last_names;
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
        return name+" "+last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthentication_token() {
        return authentication_token;
    }

    public int getId() {
        return id;
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
        dest.writeString(getAuthentication_token());
        dest.writeInt(getId());
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
