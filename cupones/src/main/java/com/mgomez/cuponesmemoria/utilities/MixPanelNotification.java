package com.mgomez.cuponesmemoria.utilities;

import android.content.Context;

import com.mgomez.cuponesmemoria.model.BeaconNotification;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.model.Notification;
import com.mgomez.cuponesmemoria.model.UserCoupon;

/**
 * Created by MGomez on 14-12-14.
 */
public class MixPanelNotification extends NotificationHub {

    private static MixPanelNotification mInstance = null;

    /**
     * Singleton
     * @param context
     * @return
     */
    public static MixPanelNotification getInstance(Context context){
        if(mInstance == null)
        {
            mInstance = new MixPanelNotification(context);
        }
        return mInstance;
    }

    Context context;

    public MixPanelNotification(Context context){
        this.context = context;
    }

    @Override
    public void userClaimedCoupon(Coupon coupon) {

    }

    @Override
    public void userUpdateListBeaconsAndCoupons(String date) {

    }

    @Override
    public void userOpenApp() {

    }

    @Override
    public void userReceivesAlert(Notification notification, BeaconNotification bn) {

    }

    @Override
    public void userReceivesCoupon(Coupon coupon, BeaconNotification bn) {

    }

    @Override
    public void userRegistered(UserCoupon userCoupon) {

    }
}
