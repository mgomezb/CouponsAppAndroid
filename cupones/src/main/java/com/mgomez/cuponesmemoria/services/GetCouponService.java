package com.mgomez.cuponesmemoria.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.connectors.CouponConnector;
import com.mgomez.cuponesmemoria.model.ObjectApiHolder;
import com.mgomez.cuponesmemoria.persistence.CouponDao;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.NotificationHub;

import java.util.Arrays;


/**
 * Created by mgomezacid on 19-05-14.
 */
public class GetCouponService extends Service {


    public static final String INIT_VERSION = "2014-05-10T11:22:45-04:00";
    static final int TIME_UPDATE = 30 * 60 * 1000; // cada media hora


    Handler handler;

    CouponDao couponDao;

    Configuration configuration;

    CouponConnector couponConnector;

    NotificationHub notificationHub;

    @Override
    public void onCreate() {
        super.onCreate();
        configuration = ((CouponApplication) getApplication()).getConfiguration();
        couponConnector = ((CouponApplication) getApplication()).getCouponConnector();
        notificationHub = ((CouponApplication) getApplication()).getNotificationHub();
        couponDao = ((CouponApplication) getApplication()).getCouponDao();
        handler = new Handler();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                handler.postDelayed(this, TIME_UPDATE);
            }
        }, 0);


        return START_STICKY;
    }

    public void loadData(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new LoadData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            new LoadData().execute();
        }
    }

    private class LoadData extends AsyncTask<Void, Void, ObjectApiHolder> {

        @Override
        protected ObjectApiHolder doInBackground(Void... params) {

            ObjectApiHolder objectApiHolder = couponConnector.getData(configuration.getProperty(getBaseContext(), Constants.API_VERSION, INIT_VERSION ));
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

                if(objectApiHolder.getAlerts()!=null)
                    couponDao.insertNotifications(Arrays.asList(objectApiHolder.getAlerts()));

                if(objectApiHolder.getCoupons()!=null){
                    couponDao.insertCoupons(Arrays.asList(objectApiHolder.getCoupons()));
                    couponDao.updateMyCoupons();
                }

            }
            return null;
        }
    }

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public GetCouponService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GetCouponService.this;
        }
    }

}
