package com.mgomez.cuponesmemoria.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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
public class NotificationActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    ListView notificationList;
    NotificationAdapter adapter;
    ArrayList<Notification> notifications;
    TextView message;
    CouponDao couponDao;
    SwipeRefreshLayout swipeView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_list);
        notificationList = (ListView) findViewById(R.id.list_notifications);
        message = (TextView) findViewById(R.id.message_no_notifications);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright), getResources().getColor(android.R.color.holo_blue_light), getResources().getColor(android.R.color.holo_blue_dark));
        swipeView.setOnRefreshListener(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        couponDao = ((CouponApplication) getApplication()).getCouponDao();


        onRefresh();

    }

    @Override
    public void onRefresh() {
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
            swipeView.setRefreshing ( true );
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
            swipeView.setRefreshing(false);
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
                if(CouponActivity.active)
                    finish();
                else{
                    startActivity(new Intent(this, CouponActivity.class));
                    finish();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
