package com.mgomez.cuponesmemoria.utilities;

import android.content.Context;
import android.provider.Settings;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.model.BeaconNotification;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.model.Notification;
import com.mgomez.cuponesmemoria.model.UserCoupon;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MGomez on 20-01-15.
 */
public class MixPanelHelper {

    MixpanelAPI m;
    MixpanelAPI.People people;
    Context context;
    Configuration configuration;

    public MixPanelHelper(Context c){
        m = MixpanelAPI.getInstance(c, Constants.MIXPANEL_TOKEN);
        configuration = ((CouponApplication) c.getApplicationContext()).getConfiguration();
        people = m.getPeople();

        final UserCoupon userCoupon = configuration.getUserCoupon(c, Constants.USER, null);
        if(userCoupon!=null) {
            m.identify(userCoupon.getRut());
            people.identify(userCoupon.getRut());
        }
        context = c;
    }

    public Context getContext() {
        return context;
    }

    /**
     * Sets global properties like the map id and AIM server.
     * @param c
     * @param p
     * @throws JSONException
     */
    private void addGlobalProperties(Context c, JSONObject p) throws JSONException {

        final String androidId = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);

        p.put(Constants.MixPanel.ANDROID_ID,androidId);
    }

    /**
     * Tracks in Mixpanel when the app is opened
     * @param c
     */
    public void trackAppOpened(Context c){

        final JSONObject p = new JSONObject();
        try {
            addGlobalProperties(c, p);

        } catch (JSONException e){}
        m.track(Constants.MixPanel.APP_OPENED, p);
    }

    /**
     * Tracks in Mixpanel that the user receives  coupon
     *
     */
    public void trackUserReceivesCoupon(Context c, Coupon coupon, BeaconNotification bn){

        final JSONObject p = new JSONObject();
        try {
            UserCoupon user = configuration.getUserCoupon(getContext(), Constants.USER, null);

            if(user != null)
                p.put(Constants.MixPanel.USER_RUT, user.getRut());
            else
                p.put(Constants.MixPanel.USER_RUT, 0);

            p.put(Constants.MixPanel.COUPON_ID, coupon.getId());
            p.put(Constants.MixPanel.COUPON_TITLE, coupon.getTitle());
            p.put(Constants.MixPanel.COUPON_TYPE, coupon.getAccess_level());

            if(bn != null) {
                p.put(Constants.MixPanel.PROXIMITY_UUID, bn.getProximity_uuid());
                p.put(Constants.MixPanel.MAJOR, bn.getMayor());
                p.put(Constants.MixPanel.MINOR, bn.getMinor());
            }
            addGlobalProperties(c, p);

        } catch (JSONException e) { }
        m.track(Constants.MixPanel.USER_RECEIVES_COUPON, p);
    }


    public void trackUserRegistered(Context c, UserCoupon userCoupon){

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        m.identify(userCoupon.getRut());
        people.identify(userCoupon.getRut());

        people.set(Constants.MixPanel.USER_RUT, userCoupon.getRut());
        people.set("$first_name", userCoupon.getNames());
        people.set("$last_name", userCoupon.getLast_names());
        people.set("$email", userCoupon.getEmail());
        people.set("$created", new DateTime().toString(fmt));
        people.set("Comuna usuario", userCoupon.getAddress());



        final JSONObject p = new JSONObject();
        try {

            p.put(Constants.MixPanel.USER_RUT, userCoupon.getRut());
            p.put(Constants.MixPanel.USER_NAME, userCoupon.getNameComplete());
            p.put(Constants.MixPanel.USER_EMAIL, userCoupon.getEmail());

            addGlobalProperties(c, p);

        } catch (JSONException e) { }
        m.track(Constants.MixPanel.USER_REGISTERED, p);

        m.flush();
    }

    /*"- RUT usuario
- Identificador cupon
- Titulo cupon"*/

    public void trackUserClaimedCoupon(Context c, Coupon coupon){

        final JSONObject p = new JSONObject();
        try {

            UserCoupon user = configuration.getUserCoupon(getContext(), Constants.USER, null);

            if(user != null)
                p.put(Constants.MixPanel.USER_RUT, user.getRut());
            else
                p.put(Constants.MixPanel.USER_RUT, 0);

            p.put(Constants.MixPanel.COUPON_ID, coupon.getId());
            p.put(Constants.MixPanel.COUPON_TITLE, coupon.getTitle());

            addGlobalProperties(c, p);

        } catch (JSONException e) { }
        m.track(Constants.MixPanel.USER_CLAIMED_COUPON, p);
    }

    public void trackUserReceivesAlert(Context c, Notification notification, BeaconNotification bn){

        final JSONObject p = new JSONObject();
        try {
            UserCoupon user = configuration.getUserCoupon(getContext(), Constants.USER, null);

            if(user != null)
                p.put(Constants.MixPanel.USER_RUT, user.getRut());
            else
                p.put(Constants.MixPanel.USER_RUT, 0);

            p.put(Constants.MixPanel.ALERT_ID, notification.getId());
            p.put(Constants.MixPanel.ALERT_TITLE, notification.getTitle());
            p.put(Constants.MixPanel.ALERT_TYPE, notification.getAccess_level());
            if(bn != null) {
                p.put(Constants.MixPanel.PROXIMITY_UUID, bn.getProximity_uuid());
                p.put(Constants.MixPanel.MAJOR, bn.getMayor());
                p.put(Constants.MixPanel.MINOR, bn.getMinor());
            }

            addGlobalProperties(c, p);

        } catch (JSONException e) { }
        m.track(Constants.MixPanel.USER_RECEIVES_ALERT, p);
    }

    /*- Fecha configuracion de beacons*/

    public void trackUserUpdateList(Context c, String date){

        final JSONObject p = new JSONObject();
        try {

            p.put(Constants.MixPanel.DATE_UPDATE_BEACONS, date);

            addGlobalProperties(c, p);

        } catch (JSONException e) { }
        m.track(Constants.MixPanel.UPDATE_LIST_COUPONS, p);
    }

    public void trackUserLoginInApp(Context c, UserCoupon userCoupon){

        m.identify(userCoupon.getRut());
        people.identify(userCoupon.getRut());

        final JSONObject p = new JSONObject();
        try {

            p.put(Constants.MixPanel.USER_RUT, userCoupon.getRut());
            p.put(Constants.MixPanel.USER_NAME, userCoupon.getNameComplete());
            p.put(Constants.MixPanel.USER_EMAIL, userCoupon.getEmail());

            addGlobalProperties(c, p);

        } catch (JSONException e) { }
        m.track(Constants.MixPanel.USER_LOGED, p);

        m.flush();
    }
}
