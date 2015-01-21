package com.mgomez.cuponesmemoria;

/**
 * Created by mgomezacid on 16-05-14.
 */
public interface Constants {

    static final String COUPON_TAG = "coupon";
    final static String API_VERSION = "api_version";

    final static String COUPON_POSITION = "coupon_position";
    final static String COUPON_TERMS = "terms_coupon";
    final static String COUPON = "c_id_center";
    final static String COUPON_CODE = "code";


    final static String SERVER = "http://couponsapplication.herokuapp.com/";
    final static String URL_GET_DATA = SERVER + "api/v1/coupons?local_id=1";
    final static String URL_REGISTER = SERVER + "api/v1/registrations";
    final static String URL_VALIDATE = SERVER + "api/v1/coupons/%1$s/claim_coupon";
    final static String URL_CATEGORIES = SERVER + "api/v1/categories";
    final static String URL_LOGIN = SERVER + "api/v1/sessions";

    final static String LAST_MODIFIED = "Last-modified";
    final static String MESSAGE = "message";
    final static String COUPON_CLAIM = "coupon_claim";
    final static String USER = "user";
    final static String NOTIFICATIONS_ACTIVATE = "notifications_activate";
    final static String COUPONS_ACTIVATE = "coupons_activate";
    final static String CATEGORY_ACTIVATE = "category_activate";

    final static String STATUS = "status";

    final static String COUPON_IS_FIRST = "coupon_is_first";
    final static String TUTORIAL_SEEN = "tutorial_seen";

    final static String GEOFENCE = "geofence";
    final static String GEOFENCE_NOTIFICATION = "geofence_notification";

    final static String GEOFENCE_ALERT = "geofence_alert";
    final public static String ALERTS_ACTIVATE = "alerts_activate";

    final static String NAME = "name";
    final static String LAST_NAME = "last_name";
    final static String RUT = "rut";
    final static String ADDRESS = "address";
    final static String GENDER = "gender";
    final static String TOKEN = "authentication_token";
    final static String EMAIL = "email";
    final static String PASSWORD = "password";
    final static String ERROR = "error";

    final static String USER_MOBILE = "mobile";

    final static String MIXPANEL_TOKEN = "c14f6536da209ec75aa8af1fa3b15768";




    public interface DB{
        public static final String DATA_BASE = "couponsm_db";
        public static final String COUPONS_TABLE = "coupons";
        public static final String NOTIFICATION_TABLE = "notifications";
        public static final String BEACONS_TABLE = "beacons";
        public static final String BEACONS_COUPONS_TABLE = "beacons_coupons";
        public static final String BEACONS_NOTIFICATIONS_TABLE = "beacons_notifications";
        public static final String MY_COUPONS_TABLE = "my_coupons";
        public static final String MY_NOTIFICATION_TABLE = "my_notifications";
        public static final String CONFIGURATION_TABLE = "configurations";
        public static final String ID = "id";
        public static final String MAYOR = "mayor";
        public static final String MINOR = "minor";
        public static final String PROXIMITY_UUID = "proximity_uuid";
        public static final String TITLE ="title";
        public static final String MESSAGE ="message";
        public static final String ACCESS_LEVEL ="access_level";
        public static final String CATEGORY_ID ="category_id";
        public static final String PROXIMITY_TRIGGER_RANGE = "proximity_trigger_range";
        public static final String IMAGE = "image";
        public static final String CODE = "code";
        public static final String INIT_DATE = "init_date";
        public static final String END_DATE = "end_date";
        public static final String RECEIVED_DATE = "received_date";
        public static final String LEGAL = "legal";
        public static final String COUPON_ID = "coupon_id";
        public static final String NOTIFICATION_ID = "notification_id";
        public static final String BEACON_ID = "beacon_id";
        public static final String VISIBLE = "visible";
        public static final String READ = "read";
        public static final String AGGREGATED = "aggregated";
        public static final String CLAIMED = "claimed";
        public static final String INSTRUCTIONS = "instructions";
        public static final String STOCK = "stock";
        public static final String STORE_NAME = "store_name";
        public static final String CATEGORIES_TABLE = "categories";
        public static final String NAME = "name";
        public static final String STORE_ID = "store_id";
        public static final String COUNT = "count";
    }

    public interface MixPanel {
        final public static String APP_OPENED = "Usuario abre aplicacion";
        final public static String ANDROID_ID = "Android id";

        final public static String USER_RECEIVES_COUPON = "Usuario recibe cupon";
        final public static String UPDATE_LIST_COUPONS = "App actualiza configuracion de beacons";
        final public static String USER_CLAIMED_COUPON = "Usuario canjea cupon";
        final public static String USER_REGISTERED = "Usuario se registra";
        final public static String USER_RECEIVES_ALERT = "Usuario recibe alerta";
        final public static String USER_RUT = "RUT usuario";

        final public static String COUPON_ID = "Identificador cupon";
        final public static String COUPON_TITLE = "Titulo cupon";
        final public static String PROXIMITY_UUID = "ProximityUUID del beacon";
        final public static String MAJOR = "Major del beacon";
        final public static String MINOR = "Minor del beacon";

        final public static String DATE_UPDATE_BEACONS = "Fecha configuracion de beacons";

        final public static String USER_NAME = "Nombre completo usuario";
        final public static String USER_EMAIL = "Email usuario";

        final public static String ALERT_ID = "Identificador alerta";
        final public static String ALERT_TITLE = "Titulo alerta";

        final public static String USER_LOGED = "Usuario se loguea";


    }
}
