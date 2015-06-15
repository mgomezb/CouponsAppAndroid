package com.mgomez.cuponesmemoria.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.R;
import com.squareup.picasso.Picasso;


/**
 * Created by mgomezacid on 11-04-14.
 */
public class TutorialFragment extends Fragment {

    TextView title, message;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.welcome_coupons, container, false);

        if(getArguments()!=null) {

            Bundle b = getArguments();

            title = (TextView) v.findViewById(R.id.title);
            message = (TextView) v.findViewById(R.id.message);
            image = (ImageView) v.findViewById(R.id.imageView);

            title.setText(getString(b.getInt("title")));
            message.setText(getString(b.getInt("message")));

            Picasso.with(getActivity()).load(b.getInt("icon")).placeholder(R.drawable.tuto_0).into(image);
        }

        return v;
    }


}
