package com.mgomez.cuponesmemoria.persistence;

import com.mgomez.cuponesmemoria.model.BeaconNotification;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.model.Notification;
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

    void insertNotifications(List<Notification> notifications);

    void insertMyNotification(Notification notification);

    ArrayList<BeaconNotification> getBeaconNotifications();

    BeaconNotification getBeaconNotification(IBeacon beacon);

    ArrayList<Coupon> getCouponsVisibles();

    ArrayList<Coupon> getMyCoupons();

    ArrayList<Notification> getMyNotifications();

    ArrayList<Notification> getNotificationsVisibles();

    ArrayList<Coupon> getCouponsFromBeaconId(int minor, int major, String proximity_uuid, int proximity_trigger_range);

    ArrayList<Notification> getNotificationsFromBeaconId(int minor, int major, String proximity_uuid, int proximity_trigger_range);

    boolean setAggregatedCoupon(long id);

    boolean setAggregateNotification(long id);

    boolean setClaimedCoupon(long id);

    boolean setReadNotification(long id);

    int getCountNotifications();

    void insertConfiguration(long id);

    void deleteConfiguration(long id);

    ArrayList<Integer> getFilterConfigurations();

    boolean isFilterConfiguration(long id);

    int getTotalConfigurations();

    boolean existCoupon(long id);

    boolean existNotification(long id);

    void updateMyCoupons();
}
