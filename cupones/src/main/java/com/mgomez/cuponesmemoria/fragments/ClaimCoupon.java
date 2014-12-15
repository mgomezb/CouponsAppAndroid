package com.mgomez.cuponesmemoria.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.activities.CouponActivity;
import com.mgomez.cuponesmemoria.connectors.CouponConnector;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.persistence.CouponDao;
import com.mgomez.cuponesmemoria.persistence.SQLiteCouponDao;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.NotificationHub;
import com.mgomez.cuponesmemoria.utilities.SharedPreferencesConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mgomezacid on 15-05-14.
 */
public class ClaimCoupon extends DialogFragment {

    public static ClaimCoupon newInstance(Coupon coupon, int position){
        ClaimCoupon f = new ClaimCoupon();
        // Supply name input as an argument.
        Bundle args = new Bundle();
        args.putInt(Constants.COUPON_POSITION, position);
        args.putParcelable(Constants.COUPON_TAG, coupon);
        f.setArguments(args);

        return f;
    }


    AlertDialog.Builder builder;
    EditText editText1, editText2, editText3, editText4;
    TextView subtitle;
    Coupon coupon;
    int position;

    CouponConnector couponConnector;

    Configuration configuration;

    NotificationHub notificationHub;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.insert_pass_coupon, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle_claim_coupon);

        coupon = getArguments().getParcelable(Constants.COUPON_TAG);
        position = getArguments().getInt(Constants.COUPON_POSITION);

        title.setText(coupon.getTitle());

        if(coupon.getUse_instructions()!=null && !coupon.getUse_instructions().equals(""))
            subtitle.setText(coupon.getUse_instructions());

        editText1= (EditText) view.findViewById(R.id.editText1);
        editText2= (EditText) view.findViewById(R.id.editText2);
        editText3= (EditText) view.findViewById(R.id.editText3);
        editText4= (EditText) view.findViewById(R.id.editText4);



        Button b = (Button) view.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText1.getText().toString() + editText2.getText().toString() + editText3.getText().toString() + editText4.getText().toString();

                if(code.equals(coupon.getCode())){
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText4.getWindowToken(), 0);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        new ValidateCoupon().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, code);
                    else
                        new ValidateCoupon().execute(code);

                }else {
                    Toast t = Toast.makeText(getActivity(), getString(R.string.coupon_validate_code_error), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            }
        });

        editText1.addTextChangedListener(editText1Listener1);
        editText2.addTextChangedListener(editText1Listener2);
        editText3.addTextChangedListener(editText1Listener3);
        editText4.addTextChangedListener(editText1Listener4);

        builder.setView(view);

        configuration = ((CouponApplication) getActivity().getApplication()).getConfiguration();
        couponConnector = ((CouponApplication) getActivity().getApplication()).getCouponConnector();
        notificationHub = ((CouponApplication) getActivity().getApplication()).getNotificationHub();

        return builder.create();
    }

    private TextWatcher editText1Listener1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>0)
                editText2.requestFocus();
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private TextWatcher editText1Listener2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==0)
                editText1.requestFocus();
            if(s.length()>0)
                editText3.requestFocus();
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private TextWatcher editText1Listener3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==0)
                editText2.requestFocus();
            if(s.length()>0)
                editText4.requestFocus();
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private TextWatcher editText1Listener4 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==0)
                editText3.requestFocus();
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private class ValidateCoupon extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... code) {
           final String token = configuration.getProperty(getActivity(), Constants.COUPON_TOKEN, "");

            return couponConnector.validateCoupon(coupon.getId(), code[0], token);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if(result!=null){
                try {
                    if (result.has(Constants.MESSAGE))
                        Toast.makeText(getActivity(), result.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();
                    else{
                        if(result.has(Constants.COUPON_CLAIM)) {
                            CouponDao couponDao = new SQLiteCouponDao(getActivity());
                            couponDao.setClaimedCoupon(coupon.getId());

                            Toast.makeText(getActivity(), getString(R.string.coupon_validate), Toast.LENGTH_LONG).show();

                            ((CouponActivity) getActivity()).setCouponClaimed(position);

                            notificationHub.userClaimedCoupon( coupon);

                            dismiss();

                        }
                    }
                    dismiss();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else
                Toast.makeText(getActivity(), getString(R.string.coupon_validate_error), Toast.LENGTH_LONG).show();

        }
    }



}
