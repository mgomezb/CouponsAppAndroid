package com.mgomez.cuponesmemoria.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.mgomez.cuponesmemoria.Constants;
import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.activities.CouponActivity;
import com.mgomez.cuponesmemoria.utilities.Configuration;


/**
 * Created by MGomez on 30-06-14.
 */
public class GeofenceIntentService extends IntentService {
    public static final String TRANSITION_INTENT_SERVICE = "ReceiveTransitionsIntentService";

    Configuration configuration;

    public GeofenceIntentService() {
        super(TRANSITION_INTENT_SERVICE);
        configuration = ((CouponApplication) getApplication()).getConfiguration();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (LocationClient.hasError(intent)) {
            //todo error process
        } else {

            Intent beaconIntent = new Intent(this, BeaconService.class);

            int transitionType = LocationClient.getGeofenceTransition(intent);
            if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ) {
                configuration.setProperty(getBaseContext(), Constants.GEOFENCE_ALERT, true);
                startService(beaconIntent);
            }
            if(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                if(isMyServiceRunning())
                    stopService(beaconIntent);

                configuration.setProperty(getBaseContext(), Constants.GEOFENCE_ALERT, false);
            }
        }
    }

    private void generateNotification() {
        long when = System.currentTimeMillis();

        Intent notifyIntent = new Intent(this, CouponActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notifyIntent.putExtra(Constants.GEOFENCE_NOTIFICATION, true);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon_notification)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Bienvenido a Parque Arauco!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setWhen(when);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, builder.build());
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BeaconService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
