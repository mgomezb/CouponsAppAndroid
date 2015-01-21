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

    public Context getContext() {
        return context;
    }

    Context context;
    MixPanelHelper mixPanelHelper;

    public MixPanelNotification(Context context){
        this.context = context;
        mixPanelHelper = new MixPanelHelper(context);
    }

    @Override
    public void userClaimedCoupon(Coupon coupon) {
        mixPanelHelper.trackUserClaimedCoupon(getContext(), coupon);
    }

    @Override
    public void userUpdateListBeaconsAndCoupons(String date) {
        mixPanelHelper.trackUserUpdateList(getContext(), date);
    }

    @Override
    public void userOpenApp() {
        mixPanelHelper.trackAppOpened(getContext());
    }

    @Override
    public void userReceivesAlert(Notification notification, BeaconNotification bn) {
        mixPanelHelper.trackUserReceivesAlert(getContext(), notification, bn);
    }

    @Override
    public void userLoginInApp(UserCoupon userCoupon) {
        mixPanelHelper.trackUserLoginInApp(getContext(), userCoupon);
    }

    @Override
    public void userReceivesCoupon(Coupon coupon, BeaconNotification bn) {
        mixPanelHelper.trackUserReceivesCoupon(getContext(), coupon, bn);
    }

    @Override
    public void userRegistered(UserCoupon userCoupon) {
        mixPanelHelper.trackUserRegistered(getContext(), userCoupon);
    }
}
