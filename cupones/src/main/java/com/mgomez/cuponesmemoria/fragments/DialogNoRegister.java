package com.mgomez.cuponesmemoria.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.activities.Register;
import com.mgomez.cuponesmemoria.model.Coupon;


/**
 * Created by mgomezacid on 16-05-14.
 */
public class DialogNoRegister extends DialogFragment {

    AlertDialog.Builder builder;

    public static DialogNoRegister newInstance(Coupon coupon){
        DialogNoRegister dialogNoRegister = new DialogNoRegister();
        // Supply name input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(Constants.COUPON_TAG, coupon);
        dialogNoRegister.setArguments(args);

        return dialogNoRegister;
    }

    private Coupon coupon;
    private Button dismiss, register;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        coupon = getArguments().getParcelable(Constants.COUPON_TAG);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.coupon_no_register, null);

        dismiss = (Button) view.findViewById(R.id.dismiss);
        register = (Button) view.findViewById(R.id.register);

        dismiss.setOnClickListener(dismissListener);
        register.setOnClickListener(registerListener);

        builder.setView(view);

        return builder.create();
    }

    private View.OnClickListener dismissListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), Register.class);
            intent.putExtra(Constants.COUPON_TAG, coupon);
            startActivity(intent);
            dismiss();
        }
    };
}
