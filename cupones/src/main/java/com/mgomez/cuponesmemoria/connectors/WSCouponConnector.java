package com.mgomez.cuponesmemoria.connectors;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.model.Category;
import com.mgomez.cuponesmemoria.model.ObjectApiHolder;
import com.mgomez.cuponesmemoria.model.UserCoupon;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.Utils;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MGomez on 13-12-14.
 */
public class WSCouponConnector implements CouponConnector {

    final GsonBuilder builder;
    final Gson gson;
    Configuration configuration;
    Context context;

    public WSCouponConnector(Context context){
        builder = new GsonBuilder();
        gson = builder.create();
        this.context = context;
        configuration = ((CouponApplication) context.getApplicationContext()).getConfiguration();
    }

    @Override
    public ObjectApiHolder getData(String version) {
        Log.d("Descargando datos", "Downloading data");

        final UserCoupon user = configuration.getUserCoupon(getContext(), Constants.USER, null);

        final String email;
        final String token;

        if(user!=null) {
            email = user.getEmail();
            token = user.getAuthentication_token();
        }
        else {
            email = "";
            token = "";
        }

        final Header headerEmail = new BasicHeader("X-User-Email", email);
        final Header headerToken = new BasicHeader("X-User-Token", token);
        ArrayList<Header> hs = new ArrayList<Header>(2);

        hs.add(headerEmail);
        hs.add(headerToken);

        String resp = Utils.getRequest(Constants.URL_GET_DATA, hs);

        ObjectApiHolder objectApiHolder = null;

        if(resp!=null && resp.contains("coupons"))
            objectApiHolder = gson.fromJson(resp, ObjectApiHolder.class);


        return objectApiHolder;
    }

    @Override
    public JSONObject userRegister(UserCoupon user, String idFacebook) {

        try {
            JSONObject json = new JSONObject();
            json.put(Constants.EMAIL, user.getEmail());
            json.put(Constants.PASSWORD, user.getPassword());
            json.put(Constants.NAME, user.getNames());
            json.put(Constants.LAST_NAME, user.getLast_names());
            json.put(Constants.RUT, user.getRut());
            json.put(Constants.ADDRESS, user.getAddress());
            json.put(Constants.GENDER, user.getGender());

            JSONObject jsonS = new JSONObject();
            jsonS.put(Constants.USER, json);
            String resp = Utils.postRequest(jsonS, Constants.URL_REGISTER);

            if(resp != null){
                return new JSONObject(resp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public JSONObject validateCoupon(long coupon_id, String coupon_code) {
        try {

            final UserCoupon user = configuration.getUserCoupon(getContext(), Constants.USER, null);

            final String email;
            final String token;

            if(user!=null) {
                email = user.getEmail();
                token = user.getAuthentication_token();
            }
            else {
                email = "";
                token = "";
            }

            final Header headerEmail = new BasicHeader("X-User-Email", email);
            final Header headerToken = new BasicHeader("X-User-Token", token);
            ArrayList<Header> hs = new ArrayList<Header>(2);

            hs.add(headerEmail);
            hs.add(headerToken);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(Constants.COUPON_CODE, coupon_code);

            final String resp = Utils.putRequest(jsonObject, String.format(Constants.URL_VALIDATE, coupon_id), hs);

            if(resp!=null)
                return new JSONObject(resp);
            else
                return null;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public List<Category> getCategories() {

        final UserCoupon user = configuration.getUserCoupon(getContext(), Constants.USER, null);

        final String email;
        final String token;

        if(user!=null) {
            email = user.getEmail();
            token = user.getAuthentication_token();
        }
        else {
            email = "";
            token = "";
        }

        final Header headerEmail = new BasicHeader("X-User-Email", email);
        final Header headerToken = new BasicHeader("X-User-Token", token);
        ArrayList<Header> hs = new ArrayList<Header>(2);


        hs.add(headerEmail);
        hs.add(headerToken);

        String resp = Utils.getRequest(Constants.URL_CATEGORIES, hs);

        ObjectApiHolder objectApiHolder = null;

        if(resp != null && resp.contains("categories"))
            objectApiHolder = gson.fromJson(resp, ObjectApiHolder.class);
        else
            return null;


        if(objectApiHolder.getCategories()!=null)
            return Arrays.asList(objectApiHolder.getCategories());
        else
            return null;
    }

    @Override
    public JSONObject login(String email, String password) {
        try {
            JSONObject json = new JSONObject();
            json.put(Constants.EMAIL, email);
            json.put(Constants.PASSWORD, password);

            JSONObject jsonS = new JSONObject();
            jsonS.put(Constants.USER, json);
            String resp = Utils.postRequest(jsonS, Constants.URL_LOGIN);

            if(resp != null){
                return new JSONObject(resp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public boolean logout() {
        try {
            final UserCoupon user = configuration.getUserCoupon(getContext(), Constants.USER, null);

            final String email;
            final String token;

            if(user!=null) {
                email = user.getEmail();
                token = user.getAuthentication_token();
            }
            else {
                email = "";
                token = "";
            }

            JSONObject json = new JSONObject();
            json.put(Constants.EMAIL, email);
            json.put(Constants.TOKEN, token);

            JSONObject jsonS = new JSONObject();
            jsonS.put(Constants.USER, json);


            int resp = Utils.deleteRequest(Constants.URL_LOGIN, json);

            if(resp == 200)
                return true;
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Context getContext(){
        return this.context;
    }
}
