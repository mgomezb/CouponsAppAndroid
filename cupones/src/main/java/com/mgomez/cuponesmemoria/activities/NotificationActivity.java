package com.mgomez.cuponesmemoria.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.CouponApplication;
import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.adapters.NotificationAdapter;
import com.mgomez.cuponesmemoria.model.Notification;
import com.mgomez.cuponesmemoria.persistence.CouponDao;
import com.mgomez.cuponesmemoria.utilities.Utils;

import java.util.ArrayList;


/**
 * Created by MGomez on 11-06-14.
 */
public class NotificationActivity extends Activity {

    ListView notificationList;
    ProgressBar progressBar;
    NotificationAdapter adapter;
    ArrayList<Notification> notifications;
    TextView message;
    CouponDao couponDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_list);
        notificationList = (ListView) findViewById(R.id.list_notifications);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        message = (TextView) findViewById(R.id.message_no_notifications);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        couponDao = ((CouponApplication) getApplication()).getCouponDao();


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new LoadData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            new LoadData().execute();
        }

    }

    private class LoadData extends AsyncTask<Void, Void, NotificationAdapter> {


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected NotificationAdapter doInBackground(Void... params) {

            notifications = couponDao.getMyNotifications();

            for(Notification a : notifications){
                if(!Utils.isCurrent(a.getEnd_date()))
                    notifications.remove(a);
            }

            return new NotificationAdapter(getBaseContext(),  notifications);

        }

        @Override
        protected void onPostExecute(final NotificationAdapter adapterResult) {
            progressBar.setVisibility(View.GONE);
            adapter = adapterResult;
            notificationList.setAdapter(adapter);

            if(adapter.getCount()==0)
                message.setVisibility(View.VISIBLE);
        }
    }

    private class ChangeStateNotifications extends AsyncTask<ArrayList<Notification>, Void, Void> {


        @Override
        protected Void doInBackground(ArrayList<Notification>... params) {

            for(Notification notification: params[0]){
                couponDao.setReadNotification(notification.getId());
            }

            return null;

        }
    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new ChangeStateNotifications().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, notifications);
        }
        else {
            new ChangeStateNotifications().execute(notifications);
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        if(item.getItemId() == R.id.configuration){
            startActivity(new Intent(NotificationActivity.this, ConfigurationActivity.class));
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
}
