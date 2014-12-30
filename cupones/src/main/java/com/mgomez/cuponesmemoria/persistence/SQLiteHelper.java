package com.mgomez.cuponesmemoria.persistence;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;



import com.mgomez.cuponesmemoria.Constants.DB;

/**
 * Created by mgomezacid on 19-05-14.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_BEACONS = "CREATE TABLE "+DB.BEACONS_TABLE+" ("+DB.ID+" integer NOT NULL, "+DB.MAYOR+" integer NOT NULL, "+DB.MINOR+" integer NOT NULL, "+DB.PROXIMITY_UUID+" integer NOT NULL);";
    private static final String CREATE_TABLE_COUPONS = "CREATE TABLE "+DB.COUPONS_TABLE+" ("+DB.ID+" integer NOT NULL, "+DB.TITLE+" text NOT NULL, "+DB.MESSAGE+" text NOT NULL, "+DB.ACCESS_LEVEL+" text NOT NULL, "+DB.LEGAL+" text NOT NULL, "+DB.CATEGORY_ID+" integer, "+DB.PROXIMITY_TRIGGER_RANGE+" integer NOT NULL, "+DB.IMAGE+" text NOT NULL, "+DB.CODE+" text NOT NULL, "+DB.INIT_DATE+" text NOT NULL, "+DB.END_DATE+" text NOT NULL, "+DB.AGGREGATED+" integer NOT NULL, "+DB.INSTRUCTIONS+" text, "+DB.STOCK+" integer, "+DB.STORE_ID+" integer, "+DB.STORE_NAME+" text NOT NULL);";
    private static final String CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE "+DB.NOTIFICATION_TABLE+" ("+DB.ID+" integer NOT NULL, "+DB.TITLE+" text NOT NULL, "+DB.MESSAGE+" text NOT NULL, "+DB.PROXIMITY_TRIGGER_RANGE+" integer NOT NULL, "+DB.INIT_DATE+" text NOT NULL, "+DB.END_DATE+" text NOT NULL, "+DB.AGGREGATED+" integer NOT NULL, "+DB.ACCESS_LEVEL+" text NOT NULL);";
    private static final String CREATE_TABLE_BEACONS_COUPONS = "CREATE TABLE "+DB.BEACONS_COUPONS_TABLE+" ("+DB.BEACON_ID+" integer NOT NULL,  "+DB.COUPON_ID+" integer NOT NULL);";
    private static final String CREATE_TABLE_BEACONS_ALERTS = "CREATE TABLE "+DB.BEACONS_NOTIFICATIONS_TABLE +" ("+DB.BEACON_ID+" integer NOT NULL,  "+DB.NOTIFICATION_ID +" integer NOT NULL);";

    private static final String CREATE_TABLE_MY_COUPONS = "CREATE TABLE "+DB.MY_COUPONS_TABLE+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+DB.ID+" integer NULL, "+DB.TITLE+" text NOT NULL, "+DB.MESSAGE+" text NOT NULL, "+DB.ACCESS_LEVEL+" text NOT NULL, "+DB.CATEGORY_ID+" integer,"+DB.LEGAL+" text NOT NULL, "+DB.PROXIMITY_TRIGGER_RANGE+" integer NOT NULL, "+DB.IMAGE+" text NOT NULL, "+DB.CODE+" text NOT NULL, "+DB.INIT_DATE+" text NOT NULL, "+DB.END_DATE+" text NOT NULL, "+DB.CLAIMED+" integer NOT NULL, "+DB.INSTRUCTIONS+" text, "+DB.STOCK+" integer, "+DB.STORE_ID+" integer, "+DB.STORE_NAME+" text NOT NULL);";
    private static final String CREATE_TABLE_MY_NOTIFICATIONS = "CREATE TABLE "+DB.MY_NOTIFICATION_TABLE+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+DB.ID+" integer NOT NULL, "+DB.TITLE+" text NOT NULL, "+DB.MESSAGE+" text NOT NULL, "+DB.PROXIMITY_TRIGGER_RANGE+" integer NOT NULL, "+DB.INIT_DATE+" text NOT NULL, "+DB.END_DATE+" text NOT NULL, "+DB.READ+" integer NOT NULL, "+DB.RECEIVED_DATE+" integer NOT NULL, "+DB.ACCESS_LEVEL+" text NOT NULL);";

    private static final String CREATE_TABLE_CONFIGURATIONS = "CREATE TABLE "+DB.CONFIGURATION_TABLE+" ("+DB.ID+" INTEGER PRIMARY KEY)";
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE "+DB.CATEGORIES_TABLE+" ("+DB.ID+" INTEGER PRIMARY KEY, "+DB.NAME+" text NOT NULL);";
    public static final int VERSION = 1;

    public SQLiteHelper(Context context){
        super(context, DB.DATA_BASE ,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_NOTIFICATIONS);
            db.execSQL(CREATE_TABLE_BEACONS);
            db.execSQL(CREATE_TABLE_COUPONS);
            db.execSQL(CREATE_TABLE_BEACONS_ALERTS);
            db.execSQL(CREATE_TABLE_BEACONS_COUPONS);
            db.execSQL(CREATE_TABLE_MY_COUPONS);
            db.execSQL(CREATE_TABLE_MY_NOTIFICATIONS);
            db.execSQL(CREATE_TABLE_CONFIGURATIONS);
            db.execSQL(CREATE_TABLE_CATEGORIES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DB.BEACONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+DB.COUPONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+DB.NOTIFICATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+DB.BEACONS_COUPONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+DB.BEACONS_NOTIFICATIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+DB.CATEGORIES_TABLE);
        onCreate(db);
    }


}
