package com.mgomez.cuponesmemoria.connectors;

import com.mgomez.cuponesmemoria.model.Category;
import com.mgomez.cuponesmemoria.model.ObjectApiHolder;
import com.mgomez.cuponesmemoria.model.UserCoupon;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by mgomezacid on 16-05-14.
 */
public interface CouponConnector {

    ObjectApiHolder getData(String version);

    JSONObject userRegister(UserCoupon user, String idFacebook);

    JSONObject validateCoupon(long coupon_id, String coupon_code);

    List<Category> getCategories();

}
