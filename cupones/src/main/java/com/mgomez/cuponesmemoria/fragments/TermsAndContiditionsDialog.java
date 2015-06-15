package com.mgomez.cuponesmemoria.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.utilities.Utils;


/**
 * Created by mgomezacid on 15-05-14.
 */
public class TermsAndContiditionsDialog extends DialogFragment {

    AlertDialog.Builder builder;

    public static TermsAndContiditionsDialog newInstance(Coupon coupon){
        TermsAndContiditionsDialog termsAndContiditionsDialog = new TermsAndContiditionsDialog();
        // Supply name input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(Constants.COUPON_TAG, coupon);
        termsAndContiditionsDialog.setArguments(args);

        return termsAndContiditionsDialog;
    }

    private Coupon coupon;
    TextView terms, date_end;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        coupon = getArguments().getParcelable(Constants.COUPON_TAG);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.coupons_terms_conditions, null);

        terms = (TextView) view.findViewById(R.id.terms);
        date_end = (TextView) view.findViewById(R.id.date_end);
        Button b = (Button) view.findViewById(R.id.button);

        terms.setText(coupon.getLegal());
        date_end.setText(getString(R.string.coupon_date_end)+" "+ Utils.getDate(coupon.getEnd_date()));

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);

        return builder.create();
    }


}
