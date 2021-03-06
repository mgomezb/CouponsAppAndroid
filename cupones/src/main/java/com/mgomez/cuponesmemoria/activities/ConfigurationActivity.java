package com.mgomez.cuponesmemoria.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.adapters.ConfigurationAdapter;
import com.mgomez.cuponesmemoria.connectors.CouponConnector;
import com.mgomez.cuponesmemoria.model.Category;
import com.mgomez.cuponesmemoria.persistence.CouponDao;
import com.mgomez.cuponesmemoria.utilities.Configuration;

import org.jraf.android.backport.switchwidget.Switch;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MGomez on 12-06-14.
 */
public class ConfigurationActivity extends Activity {


    Configuration configuration;
    ListView listView;
    public Switch generalInformation, allCategories;
    ProgressBar progressBar;
    CouponConnector couponConnector;
    ConfigurationAdapter adapter;
    CouponDao couponDao;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.coupons_configuration);

        configuration = ((CouponApplication) getApplication()).getConfiguration();
        couponConnector = ((CouponApplication) getApplication()).getCouponConnector();
        couponDao = ((CouponApplication) getApplication()).getCouponDao();

        listView = (ListView) findViewById(R.id.listView);
        generalInformation = (Switch) findViewById(R.id.information_switch);
        allCategories = (Switch) findViewById(R.id.all_category_switch);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        generalInformation.setOnCheckedChangeListener(generalInformationListener);
        allCategories.setOnClickListener(allCategoriesListener);

        generalInformation.setChecked(configuration.getProperty(getBaseContext(), Constants.NOTIFICATIONS_ACTIVATE, true));
        allCategories.setChecked(configuration.getProperty(getBaseContext(), Constants.COUPONS_ACTIVATE, true));

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new LoadData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            new LoadData().execute();
        }

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    private void setAllCategoriesFirstUsage() {
        if(configuration.getProperty(getBaseContext(), Constants.COUPON_IS_FIRST, true)){
            configuration.setProperty(getBaseContext(), Constants.COUPON_IS_FIRST, false);
            for(Category sc:adapter.getCategories()){
                couponDao.insertConfiguration(sc.getId());
            }
        }
    }

    private CompoundButton.OnCheckedChangeListener generalInformationListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            configuration.setProperty(getBaseContext(), Constants.NOTIFICATIONS_ACTIVATE, checked);
        }
    };

    private View.OnClickListener allCategoriesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            configuration.setProperty(getBaseContext(), Constants.COUPONS_ACTIVATE, allCategories.isChecked());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new SetState().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, allCategories.isChecked());
            else
                new SetState().execute(allCategories.isChecked());
        }
    };

    private void setStateSwitch(boolean state) {
        if(adapter!=null) {
            int count = adapter.getCount();

            if(state)
                for (int i = 0; i < count; i++)
                    couponDao.insertConfiguration(adapter.getItem(i).getId());
            else
                for (int i = 0; i < count; i++)
                    couponDao.deleteConfiguration(adapter.getItem(i).getId());
        }
    }

    private class SetState extends AsyncTask<Boolean, Void, Void>{
        @Override
        protected Void doInBackground(Boolean... booleans) {
            setStateSwitch(booleans[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(adapter!=null)
                adapter.notifyDataSetChanged();
        }
    }


    private class LoadData extends AsyncTask<Void, Void, ConfigurationAdapter>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ConfigurationAdapter doInBackground(Void... params) {
            final List<Category> categories = couponConnector.getCategories();
            if(categories != null)
                adapter = new ConfigurationAdapter(ConfigurationActivity.this,  categories);
            else
                adapter = new ConfigurationAdapter(ConfigurationActivity.this, new ArrayList<Category>());

            return adapter;

        }

        @Override
        protected void onPostExecute(final ConfigurationAdapter adapterResult) {
            listView.setAdapter(adapterResult);
            progressBar.setVisibility(View.GONE);
            setAllCategoriesFirstUsage();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        if(item.getItemId() == R.id.tutorial){
            Intent i = new Intent(ConfigurationActivity.this, Tutorial.class);
            i.putExtra("config", true);
            startActivity(i);
            finish();
            return true;
        }
        if(item.getItemId() == R.id.logout){
            logoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(ConfigurationActivity.this);
        b.setTitle("Cerrar Sesión");
        b.setMessage("Si cierras sesión se borrarán todos tus cupones y tendrás que volver a capturarlos.\n¿Deseas continuar?");
        b.setCancelable(false);
        b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new Logout().execute();
                dialog.dismiss();
            }
        });

        AlertDialog ad = b.create();
        ad.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.configurations, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private class Logout extends AsyncTask<Void, Boolean, Boolean>{

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(ConfigurationActivity.this, null, getString(R.string.logout_message), true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return couponConnector.logout();
        }

        @Override
        protected void onPostExecute(Boolean resp) {
            configuration.setLogOut(getBaseContext(), Constants.USER);
            couponDao.dropDataBase();
            pd.dismiss();
            finish();
        }
    }
}
