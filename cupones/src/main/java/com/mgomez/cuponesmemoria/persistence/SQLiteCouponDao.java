package com.mgomez.cuponesmemoria.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static android.provider.BaseColumns._ID;

import com.mgomez.cuponesmemoria.model.Alert;
import com.mgomez.cuponesmemoria.model.BeaconNotification;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.utilities.Utils;
import com.radiusnetworks.ibeacon.IBeacon;
import com.mgomez.cuponesmemoria.Constants.DB;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mgomezacid on 19-05-14.
 */
public class SQLiteCouponDao implements CouponDao {


    private static SQLiteCouponDao mInstance = null;

    /**
     * Singleton
     * @param context
     * @return
     */
    public static SQLiteCouponDao getInstance(Context context){
        if(mInstance == null)
        {
            mInstance = new SQLiteCouponDao(context);
        }
        return mInstance;
    }

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    public SQLiteCouponDao(Context context){
        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
    }

    @Override
    public void insertBeacons(List<BeaconNotification> beaconNotifications){
        sqLiteHelper.onUpgrade(db, db.getVersion(), db.getVersion()+1);

        for(BeaconNotification b: beaconNotifications){
            insertBeacon(b);
        }
    }

    private void insertBeacon(BeaconNotification beaconNotification) {
        ContentValues values = new ContentValues();
        values.put(DB.ID, beaconNotification.getId());
        values.put(DB.X, beaconNotification.getX());
        values.put(DB.Y, beaconNotification.getY());
        values.put(DB.FLOOR_ID, beaconNotification.getFloor_id());
        values.put(DB.MAYOR, beaconNotification.getMayor());
        values.put(DB.MINOR, beaconNotification.getMinor());
        values.put(DB.PROXIMITY_UUID, beaconNotification.getProximity_uuid());

        db.insert(DB.BEACONS_TABLE, null, values);

        insertRelationBeaconsCoupons(beaconNotification.getId(), beaconNotification.getCoupons_ids());
        insertRelationBeaconsAlerts(beaconNotification.getId(), beaconNotification.getAlerts_ids());
    }

    @Override
    public void insertCoupons(List<Coupon> coupons){

        for(Coupon c: coupons){
            insertCoupon(c);
        }
    }

    private void insertCoupon(Coupon c) {

        if(c.getAccess_level().equals(Coupon.Types.PUBLIC.toString())) {
            if(!existCoupon(c.getId()))
                insertMyCoupons(c);
        }

        ContentValues values = new ContentValues();
        values.put(DB.ID, c.getId());
        values.put(DB.TITLE, c.getTitle());
        values.put(DB.MESSAGE, c.getMessage());
        values.put(DB.ACCESS_LEVEL, c.getAccess_level());
        values.put(DB.STORE_CATEGORY_ID, c.getStore_category_id());
        values.put(DB.INTEREST_POINT_ID, c.getInterest_point_id());
        values.put(DB.PROXIMITY_TRIGGER_RANGE, c.getProximity_trigger_range());
        values.put(DB.IMAGE, c.getImage());
        values.put(DB.CODE, c.getCode());
        values.put(DB.INIT_DATE, c.getInit_date());
        values.put(DB.END_DATE, c.getEnd_date());
        values.put(DB.LEGAL, c.getLegal());
        values.put(DB.AGGREGATED, 0);
        values.put(DB.CLAIMABLE, (c.isClaimable() ? 1 : 0));
        values.put(DB.INSTRUCTIONS, c.getUse_instructions());
        values.put(DB.STOCK, c.getStock());

        db.insert(DB.COUPONS_TABLE, null, values);
    }

    @Override
    public void insertMyCoupons(Coupon c) {


        ContentValues values = new ContentValues();
        values.put(DB.ID, c.getId());
        values.put(DB.TITLE, c.getTitle());
        values.put(DB.MESSAGE, c.getMessage());
        values.put(DB.ACCESS_LEVEL, c.getAccess_level());
        values.put(DB.STORE_CATEGORY_ID, c.getStore_category_id());
        values.put(DB.INTEREST_POINT_ID, c.getInterest_point_id());
        values.put(DB.PROXIMITY_TRIGGER_RANGE, c.getProximity_trigger_range());
        values.put(DB.IMAGE, c.getImage());
        values.put(DB.CODE, c.getCode());
        values.put(DB.INIT_DATE, c.getInit_date());
        values.put(DB.END_DATE, c.getEnd_date());
        values.put(DB.LEGAL, c.getLegal());
        values.put(DB.CLAIMED, 0);
        values.put(DB.CLAIMABLE, (c.isClaimable() ? 1 : 0));
        values.put(DB.INSTRUCTIONS, c.getUse_instructions());
        values.put(DB.STOCK, c.getStock());

        db.insert(DB.MY_COUPONS_TABLE, null, values);


        setAggregatedCoupon(c.getId());

    }

    @Override
    public void insertAlerts(List<Alert> alerts){
        for(Alert a: alerts){
            insertAlert(a);
        }
    }

    private void insertAlert(Alert alert) {
        ContentValues values = new ContentValues();
        values.put(DB.ID, alert.getId());
        values.put(DB.TITLE, alert.getTitle());
        values.put(DB.MESSAGE, alert.getMessage());
        values.put(DB.PROXIMITY_TRIGGER_RANGE, alert.getProximity_trigger_range());
        values.put(DB.INIT_DATE, alert.getInit_date());
        values.put(DB.END_DATE, alert.getEnd_date());
        values.put(DB.AGGREGATED, 0);
        values.put(DB.TYPE, Alert.Types.ALERT.toString());

        db.insert(DB.NOTIFICATION_TABLE, null, values);

    }

    @Override
    public void insertMyAlert(Alert alert) {


        ContentValues values = new ContentValues();
        values.put(DB.ID, alert.getId());
        values.put(DB.TITLE, alert.getTitle());
        values.put(DB.MESSAGE, alert.getMessage());
        values.put(DB.PROXIMITY_TRIGGER_RANGE, alert.getProximity_trigger_range());
        values.put(DB.INIT_DATE, alert.getInit_date());
        values.put(DB.END_DATE, alert.getEnd_date());
        values.put(DB.READ, 0);
        values.put(DB.TYPE, Alert.Types.ALERT.toString());
        values.put(DB.RECEIVED_DATE, new DateTime().withTimeAtStartOfDay().getMillis());

        db.insert(DB.MY_NOTIFICATION_TABLE, null, values);


        setAggregateAlert(alert.getId());

    }

    private void insertRelationBeaconsCoupons(long idBeacons, int[] idsCoupons) {
        for(int i =0; i < idsCoupons.length; i++) {
            ContentValues values = new ContentValues();
            values.put(DB.BEACON_ID, idBeacons);
            values.put(DB.COUPON_ID, idsCoupons[i]);
            db.insert(DB.BEACONS_COUPONS_TABLE, null, values);
        }
    }

    private void insertRelationBeaconsAlerts(long idBeacons, int[] idsAlerts) {
        for(int i =0; i < idsAlerts.length; i++) {
            ContentValues values = new ContentValues();
            values.put(DB.BEACON_ID, idBeacons);
            values.put(DB.ALERT_ID, idsAlerts[i]);
            db.insert(DB.BEACONS_ALERTS_TABLE, null, values);
        }
    }

    @Override
    public synchronized ArrayList<BeaconNotification> getBeaconNotifications(){

        ArrayList<BeaconNotification> beaconNotifications = new ArrayList<BeaconNotification>();


        Cursor c = db.query(DB.BEACONS_TABLE, null, null, null, null,null,null);
        if(c.moveToFirst()) {
            do{
                beaconNotifications.add(createEntityBeaconNotification(c));
            }while (c.moveToNext());
        }

        c.close();

        return beaconNotifications;
    }

    @Override
    public synchronized BeaconNotification getBeaconNotification(IBeacon beacon){

        final String sql = "Select * from "+DB.BEACONS_TABLE+" where "+DB.MINOR+" = '"+beacon.getMinor()+"' " +
                           "AND "+DB.MAYOR+" = '"+beacon.getMajor()+"' " +
                           "AND "+DB.PROXIMITY_UUID+" = '"+beacon.getProximityUuid()+"' COLLATE NOCASE ";


        Cursor c = db.rawQuery(sql, null);

        BeaconNotification beaconNotification = null;

        if(c.moveToFirst())
            beaconNotification = createEntityBeaconNotification(c);

        c.close();

        return beaconNotification;

    }

    private BeaconNotification createEntityBeaconNotification(Cursor row) {
        return new BeaconNotification(row.getLong(row.getColumnIndexOrThrow(DB.ID)),
                row.getInt(row.getColumnIndexOrThrow(DB.X)),
                row.getInt(row.getColumnIndexOrThrow(DB.Y)),
                row.getInt(row.getColumnIndexOrThrow(DB.FLOOR_ID)),
                row.getInt(row.getColumnIndexOrThrow(DB.MAYOR)),
                row.getInt(row.getColumnIndexOrThrow(DB.MINOR)),
                row.getString(row.getColumnIndexOrThrow(DB.PROXIMITY_UUID))
        );
    }

    @Override
    public synchronized ArrayList<Coupon> getCouponsVisibles(){

        ArrayList<Coupon> coupons = new ArrayList<Coupon>();


        Cursor c = db.query(DB.COUPONS_TABLE, null, null, null, null,null,null);
        if(c.moveToFirst()) {
            do{
                coupons.add(createEntityCoupon(c));
            }while (c.moveToNext());
        }
        c.close();

        return coupons;
    }

    @Override
    public synchronized ArrayList<Coupon> getMyCoupons(){

        ArrayList<Coupon> coupons = new ArrayList<Coupon>();


        Cursor c = db.query(DB.MY_COUPONS_TABLE, null, null, null, null,null,_ID+" DESC");
        if(c.moveToFirst()) {
            do{
                coupons.add(createEntityMyCoupon(c));
            }while (c.moveToNext());
        }
        c.close();

        return coupons;
    }

    @Override
    public synchronized ArrayList<Alert> getMyAlerts(){

        ArrayList<Alert> alerts = new ArrayList<Alert>();


        Cursor c = db.query(DB.MY_NOTIFICATION_TABLE, null, null, null, null,null,_ID+" DESC");
        if(c.moveToFirst()) {
            do{
                alerts.add(createEntityMyAlert(c));
            }while (c.moveToNext());
        }
        c.close();

        return alerts;
    }

    private Coupon createEntityCoupon(Cursor row) {
        return new Coupon(row.getLong(row.getColumnIndexOrThrow(DB.ID)),
                row.getString(row.getColumnIndexOrThrow(DB.TITLE)),
                row.getString(row.getColumnIndexOrThrow(DB.MESSAGE)),
                row.getString(row.getColumnIndexOrThrow(DB.INIT_DATE)),
                row.getString(row.getColumnIndexOrThrow(DB.END_DATE)),
                row.getString(row.getColumnIndexOrThrow(DB.ACCESS_LEVEL)),
                row.getString(row.getColumnIndexOrThrow(DB.LEGAL)),
                row.getLong(row.getColumnIndexOrThrow(DB.STORE_CATEGORY_ID)),
                row.getLong(row.getColumnIndexOrThrow(DB.INTEREST_POINT_ID)),
                row.getInt(row.getColumnIndexOrThrow(DB.PROXIMITY_TRIGGER_RANGE)),
                row.getString(row.getColumnIndexOrThrow(DB.IMAGE)),
                row.getString(row.getColumnIndexOrThrow(DB.CODE)),
                row.getInt(row.getColumnIndexOrThrow(DB.AGGREGATED))!=0,
                row.getInt(row.getColumnIndexOrThrow(DB.CLAIMABLE))!=0,
                row.getString(row.getColumnIndexOrThrow(DB.INSTRUCTIONS)),
                row.getInt(row.getColumnIndexOrThrow(DB.STOCK))
        );
    }

    private Coupon createEntityMyCoupon(Cursor row) {
        return new Coupon(row.getLong(row.getColumnIndexOrThrow(DB.ID)),
                row.getString(row.getColumnIndexOrThrow(DB.TITLE)),
                row.getString(row.getColumnIndexOrThrow(DB.MESSAGE)),
                row.getString(row.getColumnIndexOrThrow(DB.INIT_DATE)),
                row.getString(row.getColumnIndexOrThrow(DB.END_DATE)),
                row.getString(row.getColumnIndexOrThrow(DB.ACCESS_LEVEL)),
                row.getString(row.getColumnIndexOrThrow(DB.LEGAL)),
                row.getLong(row.getColumnIndexOrThrow(DB.STORE_CATEGORY_ID)),
                row.getLong(row.getColumnIndexOrThrow(DB.INTEREST_POINT_ID)),
                row.getInt(row.getColumnIndexOrThrow(DB.PROXIMITY_TRIGGER_RANGE)),
                row.getString(row.getColumnIndexOrThrow(DB.IMAGE)),
                row.getString(row.getColumnIndexOrThrow(DB.CODE)),
                row.getInt(row.getColumnIndexOrThrow(DB.CLAIMED))!=0,
                row.getInt(row.getColumnIndexOrThrow(DB.CLAIMABLE))!=0,
                row.getString(row.getColumnIndexOrThrow(DB.INSTRUCTIONS)),
                row.getInt(row.getColumnIndexOrThrow(DB.STOCK))
        );
    }

    @Override
    public synchronized ArrayList<Alert> getAlertsVisibles(){

        ArrayList<Alert> alerts = new ArrayList<Alert>();


        Cursor c = db.query(DB.COUPONS_TABLE, null, DB.VISIBLE+" =?", new String[]{"1"}, null,null,null);
        if(c.moveToFirst()) {
            do{
                alerts.add(createEntityAlert(c));
            }while (c.moveToNext());
        }
        c.close();

        return alerts;
    }

    private Alert createEntityAlert(Cursor row) {
        return new Alert(row.getLong(row.getColumnIndexOrThrow(DB.ID)),
                row.getString(row.getColumnIndexOrThrow(DB.TITLE)),
                row.getString(row.getColumnIndexOrThrow(DB.MESSAGE)),
                row.getInt(row.getColumnIndexOrThrow(DB.PROXIMITY_TRIGGER_RANGE)),
                row.getString(row.getColumnIndexOrThrow(DB.INIT_DATE)),
                row.getString(row.getColumnIndexOrThrow(DB.END_DATE)),
                row.getInt(row.getColumnIndexOrThrow(DB.AGGREGATED))!=0,
                row.getString(row.getColumnIndexOrThrow(DB.TYPE))
        );
    }

    private Alert createEntityMyAlert(Cursor row) {

        return new Alert(row.getLong(row.getColumnIndexOrThrow(DB.ID)),
                row.getString(row.getColumnIndexOrThrow(DB.TITLE)),
                row.getString(row.getColumnIndexOrThrow(DB.MESSAGE)),
                row.getInt(row.getColumnIndexOrThrow(DB.PROXIMITY_TRIGGER_RANGE)),
                row.getString(row.getColumnIndexOrThrow(DB.INIT_DATE)),
                row.getString(row.getColumnIndexOrThrow(DB.END_DATE)),
                row.getInt(row.getColumnIndexOrThrow(DB.READ))!=0,
                true,
                row.getString(row.getColumnIndexOrThrow(DB.TYPE)),
                row.getLong(row.getColumnIndexOrThrow(DB.RECEIVED_DATE))
        );
    }

    @Override
    public synchronized ArrayList<Coupon> getCouponsFromBeaconId(int minor, int major, String proximity_uuid, int proximity_trigger_range){

        ArrayList<Coupon> coupons = new ArrayList<Coupon>();

        final String sql = "select C."+DB.ID+" as "+DB.ID+", C."+DB.TITLE+" as "+DB.TITLE+", C."+DB.MESSAGE+" as "+DB.MESSAGE+", C."+DB.INIT_DATE+" as "+DB.INIT_DATE+", " +
                "C."+DB.END_DATE+" as "+DB.END_DATE+", C."+DB.ACCESS_LEVEL+" as "+DB.ACCESS_LEVEL+", C."+DB.LEGAL+" as "+DB.LEGAL+", C."+DB.STORE_CATEGORY_ID+" as "+DB.STORE_CATEGORY_ID+", " +
                "C."+DB.INTEREST_POINT_ID+" as "+DB.INTEREST_POINT_ID+", C."+DB.PROXIMITY_TRIGGER_RANGE+" as "+DB.PROXIMITY_TRIGGER_RANGE+", C."+DB.IMAGE+" as "+DB.IMAGE+", " +
                "C."+DB.CODE+" as "+DB.CODE+", C."+DB.AGGREGATED+" as "+DB.AGGREGATED+", C."+DB.CLAIMABLE+" as "+DB.CLAIMABLE+", C."+DB.INSTRUCTIONS+" as "+DB.INSTRUCTIONS+" " +
                ", C."+DB.STOCK+" as "+DB.STOCK+" " + "from "+DB.COUPONS_TABLE+" as C " +
                "JOIN "+DB.BEACONS_COUPONS_TABLE+" as CB ON C."+DB.ID+" = CB."+DB.COUPON_ID+" " +
                "JOIN "+DB.BEACONS_TABLE+" as B ON B."+DB.ID+" = CB."+DB.BEACON_ID+" " +
                "where B."+DB.MINOR+" = '"+minor+"' " +
                "AND B."+DB.MAYOR+" = '"+major+"' " +
                "AND B."+DB.PROXIMITY_UUID+" = '"+proximity_uuid+"' COLLATE NOCASE " +
                "AND C."+DB.AGGREGATED+" = 0 " +
                "AND C."+DB.PROXIMITY_TRIGGER_RANGE+" = "+proximity_trigger_range+";";


        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            do{
                coupons.add(createEntityCoupon(c));
            }while (c.moveToNext());
        }
        c.close();

        return coupons;
    }

    @Override
    public synchronized ArrayList<Alert> getAlertsFromBeaconId(int minor, int major, String proximity_uuid, int proximity_trigger_range){

        ArrayList<Alert> alerts = new ArrayList<Alert>();

        final String sql = "select N."+DB.ID+" as "+DB.ID+", N."+DB.TITLE+" as "+DB.TITLE+", N."+DB.MESSAGE+" as "+DB.MESSAGE+", N."+DB.INIT_DATE+" as "+DB.INIT_DATE+", " +
                "N."+DB.END_DATE+" as "+DB.END_DATE+", N."+DB.PROXIMITY_TRIGGER_RANGE+" as "+DB.PROXIMITY_TRIGGER_RANGE+", N."+DB.TYPE+" as "+DB.TYPE+", " +
                "N."+DB.AGGREGATED+" as "+DB.AGGREGATED+" " +
                "from "+DB.NOTIFICATION_TABLE+" as N " +
                "JOIN "+DB.BEACONS_ALERTS_TABLE+" as NB ON N."+DB.ID+" = NB."+DB.ALERT_ID+" " +
                "JOIN "+DB.BEACONS_TABLE+" as B ON B."+DB.ID+" = NB."+DB.BEACON_ID+" " +
                "where B."+DB.MINOR+" = '"+minor+"' " +
                "AND B."+DB.MAYOR+" = '"+major+"' " +
                "AND B."+DB.PROXIMITY_UUID+" = '"+proximity_uuid+"' COLLATE NOCASE " +
                "AND N."+DB.AGGREGATED+" = 0 " +
                "AND N."+DB.PROXIMITY_TRIGGER_RANGE+" = "+proximity_trigger_range+";";


        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            do{
                alerts.add(createEntityAlert(c));
            }while (c.moveToNext());
        }
        c.close();
        return alerts;
    }

    @Override
    public boolean setAggregatedCoupon(long id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DB.AGGREGATED, 1);


        boolean update = db.update(DB.COUPONS_TABLE, initialValues ,DB.ID + " = ?", new String[]{""+id}) > 0;
        return update;
    }

    @Override
    public boolean setAggregateAlert(long id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DB.AGGREGATED, 1);

        boolean update = db.update(DB.NOTIFICATION_TABLE, initialValues ,DB.ID + " = ?", new String[]{""+id}) > 0;
        return update;
    }

    @Override
    public boolean setClaimedCoupon(long id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DB.CLAIMED, 1);

        boolean update = db.update(DB.MY_COUPONS_TABLE, initialValues ,DB.ID + " = ?", new String[]{""+id}) > 0;
        return update;
    }

    @Override
    public boolean setReadAlert(long id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DB.READ, 1);

        boolean update = db.update(DB.MY_NOTIFICATION_TABLE, initialValues ,DB.ID + " = ?", new String[]{""+id}) > 0;
        return update;
    }

    @Override
    public int getCountNotifications() {
        final String sql = "select count(*) as "+ DB.COUNT +" from "+DB.MY_NOTIFICATION_TABLE+" where "+DB.READ+" = 0";


        int count = 0;

        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            do{
                count = c.getInt(c.getColumnIndexOrThrow(DB.COUNT));

            }while (c.moveToNext());
        }
        c.close();

        return count;
    }

    @Override
    public void insertConfiguration(long id) {
        ContentValues values = new ContentValues();
        values.put(DB.ID, id);

        db.insert(DB.CONFIGURATION_TABLE, null, values);
    }

    @Override
    public void deleteConfiguration(long id) {
        db.delete(DB.CONFIGURATION_TABLE, DB.ID + " = ?", new String[]{""+id});
    }

    @Override
    public ArrayList<Integer> getFilterConfigurations() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        final String sql = "select * from "+DB.CONFIGURATION_TABLE;

        Cursor c = db.rawQuery(sql, null);

        if(c.moveToFirst()) {
            do{
                ids.add(c.getInt(c.getColumnIndexOrThrow(DB.ID)));

            }while (c.moveToNext());
        }
        c.close();

        return ids;
    }

    @Override
    public boolean isFilterConfiguration(long id) {
        final String sql = "select count(*) as "+ DB.COUNT +" from "+DB.CONFIGURATION_TABLE+" where "+DB.ID+" = "+id;

        int count = 0;

        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            do{
                count = c.getInt(c.getColumnIndexOrThrow(DB.COUNT));

            }while (c.moveToNext());
        }
        c.close();

        return count != 0;
    }

    @Override
    public synchronized int getTotalConfigurations() {
        final String sql = "select count(*) as "+ DB.COUNT +" from "+DB.CONFIGURATION_TABLE;

        int count = 0;

        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            do{
                count = c.getInt(c.getColumnIndexOrThrow(DB.COUNT));

            }while (c.moveToNext());
        }
        c.close();

        return count;
    }


    @Override
    public boolean existCoupon(long id) {
        final String sql = "select count(*) as "+ DB.COUNT +" from "+DB.MY_COUPONS_TABLE+" where "+DB.ID+" = "+id;

        int count = 0;

        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            do{
                count = c.getInt(c.getColumnIndexOrThrow(DB.COUNT));
            }while (c.moveToNext());
        }
        c.close();

        return count != 0;
    }

    @Override
    public synchronized Coupon getCouponFromIDStore(long id) {

        final String sql = "select * from "+DB.MY_COUPONS_TABLE+" where "+DB.INTEREST_POINT_ID+" = "+id + " and "+DB.CLAIMED+" = 0";

        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            return createEntityMyCoupon(c);
        }

        return null;
    }

    @Override
    public ArrayList<Long> getIdsCoupons() {

        ArrayList<Long> ids = new ArrayList<Long>();

        final String sql = "select * from "+DB.MY_COUPONS_TABLE+" where "+DB.CLAIMED+" = 0";

        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            do{
                final Coupon coupon = createEntityMyCoupon(c);
                if(Utils.isCurrent(coupon.getEnd_date()))
                    ids.add(coupon.getInterest_point_id());
            }while (c.moveToNext());
        }

        return ids;
    }

    @Override
    public boolean existAlert(long id) {

        final String sql = "select count(*) as "+ DB.COUNT +" from "+DB.MY_NOTIFICATION_TABLE+" where "+DB.ID+" = "+id;

        int count = 0;

        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            do{
                count = c.getInt(c.getColumnIndexOrThrow(DB.COUNT));
            }while (c.moveToNext());
        }
        c.close();

        return count != 0;
    }

    @Override
    public void updateMyCoupons() {
        final ArrayList<Coupon> myCoupons = getMyCoupons();
        for(Coupon c : myCoupons){
            boolean exist = false;
            for(Coupon coupon: getCouponsVisibles()){
                if(coupon.getId() == c.getId()) {
                    exist = true;
                    updateCoupon(coupon);
                }
            }
            if(!exist){
                expiredCoupon(c.getId());
            }
        }
    }

    private boolean updateCoupon(Coupon c) {

        ContentValues values = new ContentValues();
        values.put(DB.TITLE, c.getTitle());
        values.put(DB.MESSAGE, c.getMessage());
        values.put(DB.ACCESS_LEVEL, c.getAccess_level());
        values.put(DB.STORE_CATEGORY_ID, c.getStore_category_id());
        values.put(DB.INTEREST_POINT_ID, c.getInterest_point_id());
        values.put(DB.PROXIMITY_TRIGGER_RANGE, c.getProximity_trigger_range());
        values.put(DB.IMAGE, c.getImage());
        values.put(DB.CODE, c.getCode());
        values.put(DB.INIT_DATE, c.getInit_date());
        values.put(DB.END_DATE, c.getEnd_date());
        values.put(DB.LEGAL, c.getLegal());
        values.put(DB.CLAIMABLE, (c.isClaimable() ? 1 : 0));
        values.put(DB.INSTRUCTIONS, c.getUse_instructions());
        values.put(DB.STOCK, c.getStock());

        boolean update = db.update(DB.MY_COUPONS_TABLE, values ,DB.ID + " = ?", new String[]{""+c.getId()}) > 0;
        return update;
    }

    private boolean expiredCoupon(long idMyCoupon) {

        ContentValues values = new ContentValues();
        values.put(DB.END_DATE, "2014-10-08 00:00:00 -0400");

        boolean update = db.update(DB.MY_COUPONS_TABLE, values ,DB.ID + " = ?", new String[]{""+idMyCoupon}) > 0;
        return update;
    }

}
