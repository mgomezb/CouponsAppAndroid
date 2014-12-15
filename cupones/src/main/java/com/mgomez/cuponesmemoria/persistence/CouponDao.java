package com.mgomez.cuponesmemoria.persistence;

import com.mgomez.cuponesmemoria.model.Alert;
import com.mgomez.cuponesmemoria.model.BeaconNotification;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.radiusnetworks.ibeacon.IBeacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MGomez on 11-06-14.
 */
public interface CouponDao {
    void insertBeacons(List<BeaconNotification> beaconNotifications);

    void insertCoupons(List<Coupon> coupons);

    void insertMyCoupons(Coupon c);

    void insertAlerts(List<Alert> alerts);

    void insertMyAlert(Alert alert);

    ArrayList<BeaconNotification> getBeaconNotifications();

    BeaconNotification getBeaconNotification(IBeacon beacon);

    ArrayList<Coupon> getCouponsVisibles();

    ArrayList<Coupon> getMyCoupons();

    ArrayList<Alert> getMyAlerts();

    ArrayList<Alert> getAlertsVisibles();

    ArrayList<Coupon> getCouponsFromBeaconId(int minor, int major, String proximity_uuid, int proximity_trigger_range);

    ArrayList<Alert> getAlertsFromBeaconId(int minor, int major, String proximity_uuid, int proximity_trigger_range);

    boolean setAggregatedCoupon(long id);

    boolean setAggregateAlert(long id);

    boolean setClaimedCoupon(long id);

    boolean setReadAlert(long id);

    int getCountNotifications();

    void insertConfiguration(long id);

    void deleteConfiguration(long id);

    ArrayList<Integer> getFilterConfigurations();

    boolean isFilterConfiguration(long id);

    int getTotalConfigurations();

    boolean existCoupon(long id);

    Coupon getCouponFromIDStore(long id);

    ArrayList<Long> getIdsCoupons();

    boolean existAlert(long id);

    void updateMyCoupons();
}
