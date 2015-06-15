package com.mgomez.cuponesmemoria.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mgomezacid on 28-05-14.
 */

public class Comuna implements Parcelable {

    private String name;
    private String code;
    private String provincia;
    private String region;
    private String region_number;

    public Comuna(String name, String code, String provincia, String region, String region_number){
        this.name = name;
        this.code = code;
        this.provincia = provincia;
        this.region = region;
        this.region_number = region_number;
    }

    public Comuna(Parcel p){
        this.name = p.readString();
        this.code = p.readString();
        this.provincia = p.readString();
        this.region = p.readString();
        this.region_number = p.readString();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_number() {
        return region_number;
    }

    public void setRegion_number(String region_number) {
        this.region_number = region_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(provincia);
        dest.writeString(region);
        dest.writeString(region_number);
    }

    public static final Parcelable.Creator<Comuna> CREATOR = new Parcelable.Creator<Comuna>() {
        public Comuna createFromParcel(Parcel in) {
            return new Comuna(in);
        }

        public Comuna[] newArray(int size) {
            return new Comuna[size];
        }
    };
}
