package com.mgomez.cuponesmemoria.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.model.UserCoupon;


public class SharedPreferencesConfiguration implements Configuration {

    private final String STORED = "stored";

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
    }

    @Override
    public void setProperty(Context c, int key, String data) {
        setProperty(c, c.getString(key), data);
    }

    @Override
    public void setProperty(Context c, String key, String data) {
        getSharedPreferences(c).edit().putString(key, data).commit();

    }

    @Override
    public void setProperty(Context c, String key, boolean data) {
        getSharedPreferences(c).edit().putBoolean(key, data).commit();
    }

    /**
     * Creates a key for the given attribute and base
     * @param base
     * @param attribute
     * @return
     */
    private String key(String base, String attribute){
        return base + "." + attribute;
    }

    @Override
    public String getProperty(Context c, int key, String valueIfUnknown) {
        return getProperty(c, c.getString(key), valueIfUnknown);
    }

    @Override
    public String getProperty(Context c, String key, String valueIfUnknown) {
        return getSharedPreferences(c).getString(key, valueIfUnknown);
    }

    @Override
    public boolean getProperty(Context c, String key, boolean valueIfUnknown) {
        return getSharedPreferences(c).getBoolean(key, valueIfUnknown);
    }

    @Override
    public void setProperty(Context c, String key, long data) {
        getSharedPreferences(c).edit().putLong(key, data).commit();
    }

    @Override
    public long getProperty(Context c, String key, long valueIfUnknown) {
        return getSharedPreferences(c).getLong(key, valueIfUnknown);
    }

    @Override
    public void setUserCoupon(Context c, String baseKey, UserCoupon userCoupon) {
        if(userCoupon == null){
            throw new RuntimeException("Interest Point can't be null");
        }

        final SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        // internal variable used by {@link SharedPreferencesConfiguration@getInterestPoint} to check if the ip was stored
        editor.putBoolean(key(baseKey, STORED), true);

        // the remaining variables
        editor.putString(key(baseKey, Constants.NAME), userCoupon.getNames());
        editor.putString(key(baseKey, Constants.LAST_NAME), userCoupon.getLast_names());
        editor.putString(key(baseKey, Constants.RUT), userCoupon.getRut());
        editor.putString(key(baseKey, Constants.EMAIL), userCoupon.getEmail());
        editor.putString(key(baseKey, Constants.ADDRESS), userCoupon.getAddress());
        editor.putString(key(baseKey, Constants.GENDER), userCoupon.getGender());
        editor.putString(key(baseKey, Constants.TOKEN), userCoupon.getAuthentication_token());

        editor.commit();
    }

    @Override
    public UserCoupon getUserCoupon(Context c, String baseKey, UserCoupon userCoupon) {
        final SharedPreferences p = getSharedPreferences(c);
        if(p.getBoolean(key(baseKey, STORED), false)){
            return new UserCoupon(
                    p.getString(key(baseKey, Constants.NAME), null),
                    p.getString(key(baseKey, Constants.LAST_NAME), null),
                    p.getString(key(baseKey, Constants.RUT), null),
                    p.getString(key(baseKey, Constants.EMAIL), null),
                    p.getString(key(baseKey, Constants.ADDRESS), null),
                    p.getString(key(baseKey, Constants.GENDER), null),
                    p.getString(key(baseKey, Constants.TOKEN), null));
        } else {
            return userCoupon;
        }
    }

    @Override
    public void setLogOut(Context c, String baseKey) {
        final SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.putBoolean(key(baseKey, STORED), false);
        editor.commit();
    }

}
