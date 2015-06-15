package com.mgomez.cuponesmemoria.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MGomez on 17-12-14.
 */
public class Category implements Parcelable {

    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    private String name;

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Category(Parcel p){
        this.id = p.readInt();
        this.name = p.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
