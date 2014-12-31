package com.mgomez.cuponesmemoria.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.adapters.AutoCompleteAdapter;
import com.mgomez.cuponesmemoria.connectors.CouponConnector;
import com.mgomez.cuponesmemoria.model.Comuna;
import com.mgomez.cuponesmemoria.model.UserCoupon;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.NotificationHub;
import com.mgomez.cuponesmemoria.utilities.Utils;
import com.mgomez.cuponesmemoria.views.FloatLabeledAutoComplete;
import com.mgomez.cuponesmemoria.views.FloatLabeledEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


/**
 * Created by mgomezacid on 16-05-14.
 */
public class Register extends Activity {


    FloatLabeledEditText name, lastName, rut, email, genre;
    FloatLabeledAutoComplete address;
    private ArrayAdapter<String> genreAdapter;
    Button buttonRegister;
    private String userIdFacebook = "";

    private UiLifecycleHelper uiHelper;

    CouponConnector couponConnector;

    Configuration configuration;

    NotificationHub notificationHub;

    LoginButton authButton;

    UserCoupon userCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_user_register);
        setControllers();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
    }

    private void setControllers() {
        couponConnector = ((CouponApplication) getApplication()).getCouponConnector();
        configuration = ((CouponApplication) getApplication()).getConfiguration();
        notificationHub = ((CouponApplication) getApplication()).getNotificationHub();
    }

    private void findViews() {
        authButton = (LoginButton) findViewById(R.id.facebookLogin);
        name = (FloatLabeledEditText) findViewById(R.id.name);
        lastName = (FloatLabeledEditText) findViewById(R.id.last_name);
        rut = (FloatLabeledEditText) findViewById(R.id.rut);
        email = (FloatLabeledEditText) findViewById(R.id.email);
        address = (FloatLabeledAutoComplete) findViewById(R.id.address);
        genre = (FloatLabeledEditText) findViewById(R.id.genre);
        buttonRegister = (Button) findViewById(R.id.button_register);

        authButton.setReadPermissions(Arrays.asList("email"));

        name.setSingleLine(true);
        lastName.setSingleLine(true);
        rut.setSingleLine(true);
        email.setSingleLine(true);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        address.setSingleLine(true);

        genre.setEnabled(false);
        genre.setSingleLine(true);
        genre.setEditableFalse();

        genre.setOnClick(genreListener);

        setAdapterAddress();

        rut.setOnClick(rutListener);

        buttonRegister.setOnClickListener(registerListener);
    }


    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (state.isOpened()) {
                final ProgressDialog pd = ProgressDialog.show(Register.this, null, getString(R.string.loading_data), true, false);
                Log.d("FacebookSampleActivity", "Facebook session opened");
                // Request user data and show the results
                Request.newMeRequest(session, new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            // Display the parsed user info
                            userIdFacebook = user.getId();

                            name.setText(user.getFirstName());
                            lastName.setText(user.getLastName());
                            email.setText(user.getProperty("email").toString());
                            final String gender = user.getProperty("gender").toString();
                            if(gender != null){
                                if(gender.equalsIgnoreCase("male"))
                                    genre.setText(getString(R.string.male));
                                else
                                    genre.setText(getString(R.string.fale));
                            }
                        }
                        else{
                            Toast.makeText(getBaseContext(), "No pudimos precompletar tus datos", Toast.LENGTH_LONG).show();
                        }

                        pd.dismiss();
                    }

                }).executeAsync();
            } else if (state.isClosed()) {
                Log.d("FacebookSampleActivity", "Facebook session closed");
            }
        }
    };


    private void setAdapterAddress() {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();

        Comuna[] comunas = gson.fromJson(loadJSONFromAsset(), Comuna[].class);

        address.setAdapter(new AutoCompleteAdapter(getBaseContext(), Arrays.asList(comunas)));

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("comunas.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private View.OnFocusChangeListener genreListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
                genre.setError(null);
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(genre.getWindowToken(), 0);

                genreAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, new String[]{getString(R.string.male),getString(R.string.fale)});
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setAdapter(genreAdapter, onClickGenreListener);
                builder.create().show();
            }
        }
    };

    final DialogInterface.OnClickListener onClickGenreListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            genre.setText(genreAdapter.getItem(which));
        }
    };

    private View.OnFocusChangeListener rutListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                rut.setText(rut.getText().toString().replace(".","").replace("-",""));
                if (!Utils.isRut(rut.getText().toString()))
                   rut.setError(getString(R.string.rut_error));
            }
        }
    };

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(validateData()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new RegisterUser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else
                    new RegisterUser().execute();
            }
        }
    };

    private boolean validateData(){
        boolean done = true;

        if(!Utils.isRut(rut.getText().toString())){
            rut.setError(getString(R.string.rut_error));
            done = false;
        }

        if(name.getText().toString().isEmpty()){
            name.setError(getString(R.string.error_empty));
            done = false;
        }

        if(lastName.getText().toString().isEmpty()){
            lastName.setError(getString(R.string.error_empty));
            done = false;
        }

        if (!email.getText().toString().isEmpty()&&!Utils.isEmail(email.getText().toString())) {
            email.setError(getString(R.string.email_error));
            done = false;
        }

        return done;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private class RegisterUser extends AsyncTask<Void,Void, JSONObject>{

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Register.this, null, getString(R.string.registering), true, false);

            String gender ="";

            final String names = name.getText().toString();
            final String last_names = lastName.getText().toString();
            final String rutString = rut.getText().toString();
            final String emailString = email.getText().toString();

            final String addressString = address.getText().toString();
            if(!genre.getText().toString().isEmpty())
                gender = genre.getText().toString().substring(0,1);

            userCoupon = new UserCoupon(names, last_names, rutString, emailString, addressString, gender);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return couponConnector.userRegister(userCoupon, userIdFacebook);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if(result!=null){
                try {
                    if (result.has(Constants.ERROR))
                        Toast.makeText(getBaseContext(), result.getString(Constants.ERROR).replace("[", "").replace("]",""), Toast.LENGTH_LONG).show();
                    else {
                        final GsonBuilder gsonBuilder = new GsonBuilder();
                        final Gson gson = gsonBuilder.create();

                        final UserCoupon user = gson.fromJson(result.toString(), UserCoupon.class);

                        configuration.setProperty(getBaseContext(), Constants.TOKEN, user.getAuthentication_token());
                        configuration.setUserCoupon(getBaseContext(), Constants.USER, user);
                        notificationHub.userRegistered(user);

                        Toast.makeText(getBaseContext(), getString(R.string.register_done), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                        startActivity(new Intent(Register.this, CouponActivity.class));
                        finish();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else
                Toast.makeText(getBaseContext(), getString(R.string.register_error), Toast.LENGTH_LONG).show();

            pd.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Register.this, Login.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
}
