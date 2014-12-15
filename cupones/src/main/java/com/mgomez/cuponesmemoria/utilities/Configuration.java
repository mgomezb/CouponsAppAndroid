package com.mgomez.cuponesmemoria.utilities;

import android.content.Context;

import com.mgomez.cuponesmemoria.model.UserCoupon;


public interface Configuration {

    void setProperty(Context c, int key, String data);

    void setProperty(Context c, String key, String data);

    void setProperty(Context c, String key, boolean data);

    String getProperty(Context c, int key, String valueIfUnknown);

    String getProperty(Context c, String key, String valueIfUnknown);

    boolean getProperty(Context c, String key, boolean valueIfUnknown);

    void setProperty(Context c, String key, long data);

    long getProperty(Context c, String key, long valueIfUnknown);

    void setUserCoupon(Context c, String key, UserCoupon userCoupon);

    UserCoupon getUserCoupon(Context c, String key, UserCoupon userCoupon);
}
