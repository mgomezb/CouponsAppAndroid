package com.mgomez.cuponesmemoria.connectors;

import android.util.Log;

import com.mgomez.cuponesmemoria.model.ObjectApiHolder;
import com.mgomez.cuponesmemoria.model.UserCoupon;
import com.mgomez.cuponesmemoria.utilities.Utils;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MGomez on 13-12-14.
 */
public class WSCouponConnector implements CouponConnector {
    @Override
    public ObjectApiHolder getData(String version) {
        Log.d("Descargando datos", "Downloading data");

        final String url = "http://192.168.1.149:3000/api/v1/coupons?local_id=21";

        final String email = "android@mgomez.com";
        final String token = "DjFisXzNy_VLmT924ae2";

        final Header headerEmail = new BasicHeader("X-User-Email", email);
        final Header headerToken = new BasicHeader("X-User-Token", token);
        ArrayList<Header> hs = new ArrayList<Header>(2);

        hs.add(headerEmail);
        hs.add(headerToken);

        String resp = Utils.getRequest(url, hs);


        return null;
    }

    @Override
    public JSONObject userRegister(UserCoupon user, String idFacebook) {
        return null;
    }

    @Override
    public JSONObject validateCoupon(long coupon_id, String coupon_code, String token) {
        return null;
    }
}
