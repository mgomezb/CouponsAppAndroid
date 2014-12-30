package com.mgomez.cuponesmemoria;

import android.app.Application;

import com.mgomez.cuponesmemoria.connectors.CouponConnector;
import com.mgomez.cuponesmemoria.connectors.WSCouponConnector;
import com.mgomez.cuponesmemoria.persistence.CouponDao;
import com.mgomez.cuponesmemoria.persistence.SQLiteCouponDao;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.MixPanelNotification;
import com.mgomez.cuponesmemoria.utilities.NotificationHub;
import com.mgomez.cuponesmemoria.utilities.SharedPreferencesConfiguration;

/**
 * Created by MGomez on 14-12-14.
 */
public class CouponApplication extends Application {

    CouponConnector couponConnector;
    CouponDao couponDao;
    Configuration configuration;
    NotificationHub notificationHub;

    @Override
    public void onCreate() {
        super.onCreate();
        configuration = new SharedPreferencesConfiguration();
        couponConnector = new WSCouponConnector(getBaseContext());
        couponDao = SQLiteCouponDao.getInstance(getBaseContext());
        notificationHub = MixPanelNotification.getInstance(getBaseContext());
    }

    public CouponConnector getCouponConnector() {
        return couponConnector;
    }

    public CouponDao getCouponDao() {
        return couponDao;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public NotificationHub getNotificationHub(){
        return notificationHub;
    }
}
