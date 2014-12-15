package com.mgomez.cuponesmemoria.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.activities.CouponActivity;
import com.mgomez.cuponesmemoria.activities.NotificationActivity;
import com.mgomez.cuponesmemoria.model.Alert;
import com.mgomez.cuponesmemoria.model.BeaconNotification;
import com.mgomez.cuponesmemoria.model.Coupon;
import com.mgomez.cuponesmemoria.persistence.CouponDao;
import com.mgomez.cuponesmemoria.utilities.Configuration;
import com.mgomez.cuponesmemoria.utilities.NotificationHub;
import com.mgomez.cuponesmemoria.utilities.Utils;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;


import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mgomezacid on 10-04-14.
 */
public class BeaconService extends Service implements IBeaconConsumer, MonitorNotifier, RangeNotifier {

    private static final String PROXIMITY_UUID_SENION = "6E42F68A-D0D1-467B-A23E-9D11FA746E43";

    protected static final String TAG = "RangingActivity";
    private IBeaconManager iBeaconManager;
    private NotificationManager notificationManager;
    private Region regionSenionLabs;

    NotificationHub notificationHub;

    Configuration configuration;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    CouponDao couponDao;


    @Override
    public void onCreate() {
        super.onCreate();
        iBeaconManager = IBeaconManager.getInstanceForApplication(this);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        couponDao = ((CouponApplication) getApplication()).getCouponDao();
        configuration = ((CouponApplication) getApplication()).getConfiguration();
        notificationHub = ((CouponApplication) getApplication()).getNotificationHub();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        iBeaconManager.bind(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        iBeaconManager.unBind(this);
        super.onDestroy();
    }


    @Override
    public void onIBeaconServiceConnect() {
        iBeaconManager.setMonitorNotifier(this);
        iBeaconManager.setRangeNotifier(BeaconService.this);
        regionSenionLabs = new Region("cl.acid.aim.coupons.services.BeaconService", PROXIMITY_UUID_SENION, null, null);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new InitMonitoringBeaconsInRegion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            new InitMonitoringBeaconsInRegion().execute();
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new InitRangingBeaconsInRegion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            new InitRangingBeaconsInRegion().execute();
        }
    }

    @Override
    public void didExitRegion(Region region) {
        try {
            iBeaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.i(TAG, "I have just switched from seeing/not seeing iBeacons: " + state);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {

        for (IBeacon beacon: iBeacons) {
            createNotification(couponDao.getCouponsFromBeaconId(beacon.getMinor(), beacon.getMajor(), beacon.getProximityUuid(), beacon.getProximity()), beacon);

            if(configuration.getProperty(getBaseContext(), Constants.ALERTS_ACTIVATE, true))
                createNotificationAlert(couponDao.getAlertsFromBeaconId(beacon.getMinor(), beacon.getMajor(), beacon.getProximityUuid(), beacon.getProximity()), beacon);
        }

    }


    private void createNotificationAlert(ArrayList<Alert> alerts, IBeacon beacon) {

        if(alerts.size()>0) {

            final BeaconNotification bn = couponDao.getBeaconNotification(beacon);

            final long[] vibrate = {300, 1000};

            Intent resultIntent = new Intent(this, NotificationActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            for(Alert a : alerts) {

                if(!couponDao.existAlert(a.getId()) && Utils.isCurrent(a.getEnd_date())) {
                    couponDao.insertMyAlert(a);

                    notificationHub.userReceivesAlert(a, bn);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication())
                            .setSmallIcon(R.drawable.icon_notification)
                            .setContentTitle(a.getTitle())
                            .setContentText(a.getMessage())
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true)
                            .setVibrate(vibrate)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

                    // Builds the notification and issues it.
                    notificationManager.notify((int) a.getId(), mBuilder.build());
                }
            }
        }

    }

    private void createNotification(ArrayList<Coupon> coupons, IBeacon beacon){

        if(coupons.size()>0) {

            final BeaconNotification bn = couponDao.getBeaconNotification(beacon);

            long[] vibrate = {300, 1000};
            Intent resultIntent = new Intent(this, CouponActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            ArrayList<Coupon> listToRemove = new ArrayList<Coupon>();

            for(Coupon c: coupons){
                if(!couponDao.existCoupon(c.getId()) && Utils.isCurrent(c.getEnd_date())){
                    if(configuration.getProperty(getBaseContext(), Constants.COUPONS_ACTIVATE, true)){
                        couponDao.insertMyCoupons(c);
                        notificationHub.userReceivesCoupon(c, bn);
                    }
                    else {
                        if( couponDao.isFilterConfiguration(c.getStore_category_id())) {
                            couponDao.insertMyCoupons(c);
                            notificationHub.userReceivesCoupon(c, bn);
                        }
                        else
                            listToRemove.add(c);
                    }
                }
                else {
                    listToRemove.add(c);
                    couponDao.setAggregatedCoupon(c.getId());
                }
            }

            coupons.removeAll(listToRemove);

            if(coupons.size()>0) {
                String message = "Tienes ";
                if(coupons.size() == 1)
                    message = message + "1 cupón";
                else
                    message = message + coupons.size() + " cupones";


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication())
                        .setSmallIcon(R.drawable.icon_notification)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setVibrate(vibrate)
                        .setContentIntent(resultPendingIntent)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                // Builds the notification and issues it.
                notificationManager.notify((int) bn.getId(), mBuilder.build());
            }
        }

    }



    private class InitMonitoringBeaconsInRegion extends AsyncTask<Void,Void, Void>{

        @Override
        protected void onPreExecute() {
            iBeaconManager.setForegroundBetweenScanPeriod(10000);
            iBeaconManager.setForegroundScanPeriod(10000);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                iBeaconManager.startMonitoringBeaconsInRegion(regionSenionLabs);
            } catch (RemoteException e) {   }
            return null;
        }
    }

    private class InitRangingBeaconsInRegion extends AsyncTask<Void,Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try {
                iBeaconManager.startRangingBeaconsInRegion(regionSenionLabs);
            } catch (RemoteException e) {   }
            return null;
        }
    }
}

