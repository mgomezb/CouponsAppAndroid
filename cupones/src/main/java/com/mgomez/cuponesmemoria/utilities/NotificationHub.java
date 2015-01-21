package com.mgomez.cuponesmemoria.utilities;

import com.mgomez.cuponesmemoria.model.BeaconNotification;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.model.Notification;
import com.mgomez.cuponesmemoria.model.UserCoupon;


/**
 * Created by mgomez on 13-01-14.
 */
public abstract class NotificationHub {

    public abstract void userOpenApp();

    public abstract void userReceivesCoupon(Coupon coupon, BeaconNotification bn);

    public abstract void userUpdateListBeaconsAndCoupons(String date);

    public abstract void userClaimedCoupon(Coupon coupon);

    public abstract void userRegistered(UserCoupon userCoupon);

    public abstract void userReceivesAlert(Notification notification, BeaconNotification bn);

    public abstract void userLoginInApp(UserCoupon userCoupon);

}
