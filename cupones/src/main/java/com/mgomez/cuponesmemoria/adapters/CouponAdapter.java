package com.mgomez.cuponesmemoria.adapters;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.SharedPreferencesConfiguration;
import com.squareup.picasso.Picasso;

import java.util.List;


import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.fragments.ClaimCoupon;
import com.mgomez.cuponesmemoria.fragments.DialogNoRegister;
import com.mgomez.cuponesmemoria.fragments.TermsAndContiditionsDialog;
import com.mgomez.cuponesmemoria.model.Coupon;

/**
 * Created by mgomezacid on 06-05-14.
 */
public class CouponAdapter extends ArrayAdapter<Coupon> {

    Context context;
    final LayoutInflater layoutInflater;
    final FragmentManager fm;
    Configuration configuration;


    public CouponAdapter(Context context, List<Coupon> coupons, FragmentManager fm){
        super(context, R.layout.coupon_item, coupons);
        configuration = ((CouponApplication) context.getApplicationContext()).getConfiguration();
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fm = fm;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CouponHolder couponHolder;
        final Coupon coupon = getItem(position);

        if(v == null) {
            v = layoutInflater.inflate(R.layout.coupon_item, parent, false);
        }

        couponHolder = (CouponHolder) v.getTag();

        if(couponHolder == null){
            couponHolder = new CouponHolder(v);
            v.setTag(couponHolder);
        }

        couponHolder.title.setText(coupon.getTitle());
        couponHolder.message.setText(coupon.getMessage());
        couponHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = TermsAndContiditionsDialog.newInstance(coupon);
                dialog.show(fm, "TermsAndContiditionsDialog");
            }
        });
        if(coupon.isClaimable()) {
            if(coupon.getStock()>0) {
                if (coupon.isClaimed()) {
                    couponHolder.getCoupon.setEnabled(false);
                    couponHolder.getCoupon.setText(context.getString(R.string.coupon_claimed));
                    Picasso.with(context).load(R.drawable.cupon_tag_claimed).into(couponHolder.enableDisableCoupon);
                } else {
                    Picasso.with(context).load(R.drawable.cupon_tag).into(couponHolder.enableDisableCoupon);
                    couponHolder.getCoupon.setEnabled(true);
                    couponHolder.getCoupon.setText(context.getString(R.string.claim_coupon));
                    couponHolder.getCoupon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (configuration.getProperty(context, Constants.COUPON_TOKEN, null) != null) {
                                DialogFragment dialog = ClaimCoupon.newInstance(coupon, position);
                                dialog.show(fm, "ClaimCoupon");
                            } else {
                                DialogFragment dialogFragment = DialogNoRegister.newInstance(coupon);
                                dialogFragment.show(fm, "DialogNoRegister");
                            }
                        }
                    });
                }
            }
            else{
                couponHolder.getCoupon.setEnabled(false);
                couponHolder.getCoupon.setText("CUPÓN AGOTADO");
                Picasso.with(context).load(R.drawable.cupon_tag_claimed).into(couponHolder.enableDisableCoupon);
            }
        }
        else {
            couponHolder.getCoupon.setEnabled(false);
            couponHolder.getCoupon.setText(context.getString(R.string.coupon_no_claimable));
            Picasso.with(context).load(R.drawable.cupon_tag).into(couponHolder.enableDisableCoupon);
        }

        Picasso.with(context).load(coupon.getUrlImage()).centerCrop().fit().placeholder(R.drawable.imgplaceholder3).into(couponHolder.image);
        return  v;
    }

    public int getPosition(long id){
        for(int i = 0; i < getCount(); i++){
            if((getItem(i).getId() == id))
                return getPosition(getItem(i));
        }
        return 0;
    }


    static class CouponHolder{
        ImageView image, enableDisableCoupon;
        TextView title, message;
        ImageButton info;
        Button getCoupon;

        public CouponHolder(View v){
            image = (ImageView) v.findViewById(R.id.coupon_image);
            message = (TextView) v.findViewById(R.id.message);
            title = (TextView) v.findViewById(R.id.coupon_title);
            info = (ImageButton) v.findViewById(R.id.terms);
            getCoupon = (Button) v.findViewById(R.id.getCoupon);
            enableDisableCoupon = (ImageView) v.findViewById(R.id.imageView);
        }
    }

}
