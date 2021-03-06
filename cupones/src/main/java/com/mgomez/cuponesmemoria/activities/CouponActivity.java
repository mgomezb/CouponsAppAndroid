package com.mgomez.cuponesmemoria.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.adapters.CouponAdapter;
import com.mgomez.cuponesmemoria.connectors.CouponConnector;
import com.mgomez.cuponesmemoria.fragments.GeofenceFragment;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.model.ObjectApiHolder;
import com.mgomez.cuponesmemoria.persistence.CouponDao;
import com.mgomez.cuponesmemoria.services.GetCouponService;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.NotificationHub;
import com.mgomez.cuponesmemoria.utilities.Utils;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by mgomezacid on 06-05-14.
 */
public class CouponActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    ListView couponsList;
    CouponAdapter adapter;
    long couponIP;
    TextView message;
    SwipeRefreshLayout swipeView;


    CouponDao couponDao;
    Configuration configuration;
    CouponConnector couponConnector;
    NotificationHub notificationHub;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        couponIP = getIntent().getLongExtra(Constants.COUPON, 0);
        setContentView(R.layout.coupons_list);
        couponsList = (ListView) findViewById(R.id.list_coupons);
        message = (TextView) findViewById(R.id.message_no_coupons);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright), getResources().getColor(android.R.color.holo_blue_light), getResources().getColor(android.R.color.holo_blue_dark));
        swipeView.setOnRefreshListener(this);


        couponDao = ((CouponApplication) getApplication()).getCouponDao();
        configuration = ((CouponApplication) getApplication()).getConfiguration();
        couponConnector = ((CouponApplication) getApplication()).getCouponConnector();
        notificationHub = ((CouponApplication) getApplication()).getNotificationHub();

        startService(new Intent(CouponActivity.this, GetCouponService.class));
        startGeofenceService();
        loadData();
        onRefresh();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(configuration.getUserCoupon(getBaseContext(), Constants.USER, null) == null)
            finish();
    }

    private synchronized void loadData() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new LoadData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            new LoadData().execute();
        }
    }

    public void setCouponClaimed(int position){
        adapter.getItem(position).setClaimed(true);
        adapter.notifyDataSetChanged();
    }

    private void startGeofenceService() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled() && configuration.getProperty(getBaseContext(), Constants.GEOFENCE_ALERT, false)) {
                showBLEMessage();
            }
            if(configuration.getProperty(getBaseContext(), Constants.GEOFENCE, true)) {
                final FragmentTransaction t = getFragmentManager().beginTransaction();
                t.add(new GeofenceFragment(), "GeofenceFragment");
                t.commit();
                configuration.setProperty(getBaseContext(), Constants.GEOFENCE, false);
            }
        }
    }

    private void showBLEMessage(){
        AlertDialog.Builder b = new AlertDialog.Builder(CouponActivity.this);
        b.setTitle("Bluetooth Desactivado");
        b.setMessage("Para poder recibir cupones dentro del Centro Comercial debes activar el bluetooth de tu dispositivo");
        b.setCancelable(false);
        b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter.enable();
            }
        });
        AlertDialog ad = b.create();
        ad.show();
    }

    @Override
    public void onRefresh() {
        updateData();
    }


    private class LoadData extends AsyncTask<Void, Void, CouponAdapter>{

        @Override
        protected void onPreExecute() {
            swipeView.post ( new  Runnable ()  {
                @Override  public  void run ()  {
                    swipeView.setRefreshing ( true );
                }
            });
        }

        @Override
        protected CouponAdapter doInBackground(Void... params) {

            ArrayList<Coupon> coupons = couponDao.getMyCoupons();
            ArrayList<Coupon> removeCoupon = new ArrayList<Coupon>(coupons.size());

            for(Coupon c : coupons){
                if(!Utils.isCurrent(c.getEnd_date()))
                    removeCoupon.add(c);
            }

            coupons.removeAll(removeCoupon);

            return new CouponAdapter(CouponActivity.this,  coupons, CouponActivity.this.getFragmentManager());

        }

        @Override
        protected void onPostExecute(final CouponAdapter adapterResult) {
            swipeView.setRefreshing(false);
            adapter = adapterResult;
            couponsList.setAdapter(adapter);

            if(couponIP != 0){
                couponsList.setSelection(adapter.getPosition(couponIP));
            }

            if(adapter.getCount()==0)
                message.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        if(item.getItemId() == R.id.configuration){
            startActivity(new Intent(CouponActivity.this, ConfigurationActivity.class));
            return true;
        }
        if(item.getItemId() == R.id.notifications){
            startActivity(new Intent(CouponActivity.this, NotificationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.coupons, menu);

        return super.onCreateOptionsMenu(menu);
    }

    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    private void updateData() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new GetDataFromApi().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            new GetDataFromApi().execute();
        }
    }

    private class GetDataFromApi extends AsyncTask<Void, Void, ObjectApiHolder> {

        @Override
        protected ObjectApiHolder doInBackground(Void... params) {

            ObjectApiHolder objectApiHolder = couponConnector.getData(configuration.getProperty(getBaseContext(), Constants.API_VERSION, GetCouponService.INIT_VERSION ));
            return objectApiHolder;
        }

        @Override
        protected void onPostExecute(ObjectApiHolder data) {

            if(data!=null){
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
                    new SetDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
                }
                else {
                    new SetDB().execute(data);
                }
            }
        }
    }

    private class SetDB extends AsyncTask<ObjectApiHolder, Void, Void>{

        @Override
        protected Void doInBackground(ObjectApiHolder... params) {

            final ObjectApiHolder objectApiHolder = params[0];

            if(objectApiHolder!=null){

                if(objectApiHolder.getVersion()!=null) {
                    configuration.setProperty(getBaseContext(), Constants.API_VERSION, objectApiHolder.getVersion());
                    notificationHub.userUpdateListBeaconsAndCoupons(objectApiHolder.getVersion());
                }

                if(objectApiHolder.getBeaconNotifications()!=null)
                    couponDao.insertBeacons(Arrays.asList(objectApiHolder.getBeaconNotifications()));

                if(objectApiHolder.getAlerts()!=null) {
                    couponDao.insertNotifications(Arrays.asList(objectApiHolder.getAlerts()));
                    couponDao.updateMyNotifications();
                }

                if(objectApiHolder.getCoupons()!=null){
                    couponDao.insertCoupons(Arrays.asList(objectApiHolder.getCoupons()));
                    couponDao.updateMyCoupons();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadData();
        }
    }
}