package com.mgomez.cuponesmemoria.fragments;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.services.GeofenceIntentService;

import java.util.ArrayList;


/**
 * Created by MGomez on 30-06-14.
 */
public class GeofenceFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationClient.OnAddGeofencesResultListener {

    public static final float LATITUDE = (float) -33.402056;
    public static final float LONGITUDE = (float) -70.577826;
    public static final float RADIUS = (float) 700.0D;

 //Acid   //-33.420211, -70.601320
// Arauco 33.402056
    //70.577826

    private LocationClient locationClient;
    private LocationRequest locationRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resp == ConnectionResult.SUCCESS) {
            locationClient = new LocationClient(getActivity(), this, this);
            locationClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5 * 50 * 1000);
        locationRequest.setFastestInterval(5 * 50 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        ArrayList<Geofence> geofenceList = new ArrayList<Geofence>();

        Geofence geofence = new Geofence.Builder()
                .setRequestId(getString(R.string.app_name))
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(LATITUDE, LONGITUDE, RADIUS)
                .setLoiteringDelay(60000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();

        geofenceList.add(geofence);


        PendingIntent geoFencePendingIntent = PendingIntent.getService(getActivity(), 0, new Intent(getActivity(), GeofenceIntentService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        locationClient.addGeofences(geofenceList, geoFencePendingIntent, this);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onAddGeofencesResult(int i, String[] strings) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onDestroy();
        if (null != locationClient) {
            locationClient.disconnect();
        }
    }
}
