package com.mgomez.cuponesmemoria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgomez.cuponesmemoria.model.Notification;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.mgomez.cuponesmemoria.R;
import com.mgomez.cuponesmemoria.utilities.Utils;

/**
 * Created by MGomez on 11-06-14.
 */
public class NotificationAdapter extends ArrayAdapter<Notification> {

    Context context;
    final LayoutInflater layoutInflater;


    public NotificationAdapter(Context context, List<Notification> notifications) {
        super(context, R.layout.notification_item, notifications);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        NotificationHolder notificationHolder;
        final Notification notification = getItem(position);

        if(v == null) {
            v = layoutInflater.inflate(R.layout.notification_item, parent, false);
        }

        notificationHolder = (NotificationHolder) v.getTag();

        if(notificationHolder == null){
            notificationHolder = new NotificationHolder(v);
            v.setTag(notificationHolder);
        }

        notificationHolder.title.setText(notification.getTitle().toUpperCase());
        notificationHolder.message.setText(notification.getMessage());

        notificationHolder.date.setText(Utils.getTextDays(notification.getDays()));

        if(notification.isRead()) {
            notificationHolder.title.setTextColor(getContext().getResources().getColor(R.color.title_notifications_read));
            notificationHolder.message.setTextColor(getContext().getResources().getColor(R.color.message_notifications_read));
            notificationHolder.date.setTextColor(getContext().getResources().getColor(R.color.date_notifications_read));
            notificationHolder.background_notification.setBackgroundColor(getContext().getResources().getColor(R.color.background_notifications_read));
            Picasso.with(context).load(R.drawable.alert_idle).placeholder(R.drawable.alert_idle).into(notificationHolder.icon);
        }
        else {
            notificationHolder.title.setTextColor(getContext().getResources().getColor(R.color.title_notifications_no_read));
            notificationHolder.message.setTextColor(getContext().getResources().getColor(R.color.message_notifications_no_read));
            notificationHolder.date.setTextColor(getContext().getResources().getColor(R.color.date_notifications_no_read));
            notificationHolder.background_notification.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            Picasso.with(context).load(R.drawable.alert_active).placeholder(R.drawable.alert_active).into(notificationHolder.icon);
        }
        return v;

    }


    static class NotificationHolder{
        RelativeLayout background_notification;
        ImageView icon;
        TextView title, message, date;

        public NotificationHolder(View v){
            background_notification = (RelativeLayout) v.findViewById(R.id.notification_background);
            icon = (ImageView) v.findViewById(R.id.iconNotification);
            title = (TextView) v.findViewById(R.id.notification_title);
            message = (TextView) v.findViewById(R.id.notification_text);
            date = (TextView) v.findViewById(R.id.notification_date);
        }
    }
}
