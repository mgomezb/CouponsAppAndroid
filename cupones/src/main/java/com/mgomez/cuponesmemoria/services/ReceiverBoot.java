package com.mgomez.cuponesmemoria.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.fragments.GeofenceFragment;

import java.util.ArrayList;


/**
 * Created by mgomezacid on 15-05-14.
 */
public class ReceiverBoot extends BroadcastReceiver implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationClient.OnAddGeofencesResultListener{

    static final int ONE_MINUTE = 60000;
    Context context;
    LocationClient locationClient;
    LocationRequest locationRequest;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resp == ConnectionResult.SUCCESS) {
            locationClient = new LocationClient(context, this, this);
            locationClient.connect();
        }

        Intent intentGetCoupons = new Intent();
        intentGetCoupons.setAction("cl.acid.aim.coupons.services.GetCouponService");
        context.startService(intentGetCoupons);

    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d("onConnected", "Init Service");
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5 * 50 * 1000);
        locationRequest.setFastestInterval(5 * 50 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        ArrayList<Geofence> geofenceList = new ArrayList<Geofence>();

        Geofence geofence = new Geofence.Builder()
                .setRequestId(context.getString(R.string.app_name))
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(GeofenceFragment.LATITUDE, GeofenceFragment.LONGITUDE, GeofenceFragment.RADIUS)
                .setLoiteringDelay(ONE_MINUTE)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();

        geofenceList.add(geofence);


        PendingIntent geoFencePendingIntent = PendingIntent.getService(context, 0, new Intent(context, GeofenceIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        locationClient.addGeofences(geofenceList, geoFencePendingIntent, this);

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", connectionResult.toString());
    }

    @Override
    public void onAddGeofencesResult(int i, String[] strings) {

    }
}



