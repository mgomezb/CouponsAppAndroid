package com.mgomez.cuponesmemoria;

/**
 * Created by mgomezacid on 16-05-14.
 */
public interface Constants {

    static final String COUPON_TAG = "coupon";
    final static String COUPON_TOKEN = "token";
    final static String API_VERSION = "api_version";

    final static String COUPON_POSITION = "coupon_position";
    final static String COUPON_TERMS = "terms_coupon";
    final static String COUPON = "c_id_center";


    final static String SERVER = "http://indoorstaging.parquearauco.cl/";
    final static String URL_GET_DATA = SERVER + "api/v1/maps/1/notification_beacons";
    final static String URL_REGISTER = SERVER + "api/v1/maps/1/users";
    final static String URL_VALIDATE = SERVER + "api/v1/maps/1/coupon_claims";

    final static String LAST_MODIFIED = "Last-modified";
    final static String MESSAGE = "message";
    final static String COUPON_CLAIM = "coupon_claim";
    final static String USER = "user";
    final static String ALERTS_ACTIVATE = "alerts_activate";
    final static String COUPONS_ACTIVATE = "coupons_activate";
    final static String CATEGORY_ACTIVATE = "category_activate";

    final static String COUPON_IS_FIRST = "coupon_is_first";
    final static String TUTORIAL_SEEN = "tutorial_seen";

    final static String GEOFENCE = "geofence";
    final static String GEOFENCE_NOTIFICATION = "geofence_notification";

    final static String GEOFENCE_ALERT = "geofence_alert";

    public interface DB{
        public static final String DATA_BASE = "coupons_db";
        public static final String COUPONS_TABLE = "coupons";
        public static final String NOTIFICATION_TABLE = "notifications";
        public static final String BEACONS_TABLE = "beacons";
        public static final String BEACONS_COUPONS_TABLE = "beacons_coupons";
        public static final String BEACONS_ALERTS_TABLE = "beacons_alerts";
        public static final String MY_COUPONS_TABLE = "my_coupons";
        public static final String MY_NOTIFICATION_TABLE = "my_notifications";
        public static final String CONFIGURATION_TABLE = "configurations";
        public static final String COUNT = "count";
        public static final String ID = "id";
        public static final String X = "x";
        public static final String Y = "y";
        public static final String FLOOR_ID = "floor_id";
        public static final String MAYOR = "mayor";
        public static final String MINOR = "minor";
        public static final String PROXIMITY_UUID = "proximity_uuid";
        public static final String TITLE ="title";
        public static final String MESSAGE ="message";
        public static final String ACCESS_LEVEL ="access_level";
        public static final String STORE_CATEGORY_ID ="store_category_id";
        public static final String INTEREST_POINT_ID ="interest_point_id";
        public static final String PROXIMITY_TRIGGER_RANGE = "proximity_trigger_range";
        public static final String IMAGE = "image";
        public static final String CODE = "code";
        public static final String INIT_DATE = "init_date";
        public static final String END_DATE = "end_date";
        public static final String RECEIVED_DATE = "received_date";
        public static final String LEGAL = "legal";
        public static final String COUPON_ID = "coupon_id";
        public static final String ALERT_ID = "alert_id";
        public static final String BEACON_ID = "beacon_id";
        public static final String VISIBLE = "visible";
        public static final String READ = "read";
        public static final String TYPE = "type";
        public static final String AGGREGATED = "aggregated";
        public static final String CLAIMED = "claimed";
        public static final String CLAIMABLE = "claimable";
        public static final String INSTRUCTIONS = "instructions";
        public static final String STOCK = "stock";

    }
}
