package com.mgomez.cuponesmemoria.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.connectors.CouponConnector;
import com.mgomez.cuponesmemoria.model.UserCoupon;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.NotificationHub;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MGomez on 30-12-14.
 */
public class Login extends Activity {

    EditText email, password;
    Configuration configuration;
    Button loginButton, registerButton;
    CouponConnector couponConnector;
    NotificationHub notificationHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        configuration = ((CouponApplication) getApplication()).getConfiguration();
        couponConnector = ((CouponApplication) getApplication()).getCouponConnector();
        notificationHub = ((CouponApplication) getApplication()).getNotificationHub();

        notificationHub.userOpenApp();

        if(configuration.getUserCoupon(getBaseContext(), Constants.USER, null) != null){
            initCouponActivity();
        }

        setViews();
    }

    private void setViews() {
        loginButton = (Button) findViewById(R.id.login);
        registerButton = (Button) findViewById(R.id.register);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void login(){
        new LoginTask().execute();
    }

    private void register(){
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
        finish();
    }

    private void initCouponActivity(){
        Intent i = new Intent(Login.this, CouponActivity.class);
        startActivity(i);
        finish();
    }

    private class LoginTask extends AsyncTask <Void, JSONObject, JSONObject>{

        String emailString, passwordString;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Login.this, null, getString(R.string.identifying), true, false);
            emailString = email.getText().toString();
            passwordString = password.getText().toString();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return couponConnector.login(emailString, passwordString);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if(result!=null){
                try {
                    if (result.has(Constants.ERROR))
                        Toast.makeText(getBaseContext(), result.getString(Constants.ERROR).replace("[", "").replace("]", ""), Toast.LENGTH_LONG).show();
                    else {
                        final GsonBuilder gsonBuilder = new GsonBuilder();
                        final Gson gson = gsonBuilder.create();

                        final UserCoupon user = gson.fromJson(result.toString(), UserCoupon.class);

                        if(user.getUser_type().equals(Constants.USER_MOBILE)) {
                            configuration.setProperty(getBaseContext(), Constants.TOKEN, user.getAuthentication_token());
                            configuration.setUserCoupon(getBaseContext(), Constants.USER, user);
                            pd.dismiss();
                            notificationHub.userLoginInApp(user);
                            initCouponActivity();
                        }
                        else
                            Toast.makeText(getBaseContext(), getString(R.string.user_error), Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else
                Toast.makeText(getBaseContext(), getString(R.string.login_error), Toast.LENGTH_LONG).show();

            pd.dismiss();
        }
    }
}
