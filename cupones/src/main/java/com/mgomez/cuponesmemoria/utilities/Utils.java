package com.mgomez.cuponesmemoria.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getTextDays(int days) {
        if(days == 0){
            return "Hoy";
        }
        if(days == 1)
            return "Ayer";
        else
            return "Hace "+days+" días";

    }

    public static String getDateTime(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        return df.format(c.getTime());
    }

    public static String getRequest(String uri){

        HttpClient httpClient = new DefaultHttpClient(getParams());
        HttpGet getRequest = new HttpGet(uri);
        getRequest.addHeader("Content-type", "application/json");
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Authorization", "Basic");

        try {
            HttpResponse respuesta = httpClient.execute(getRequest);
            int code = respuesta.getStatusLine().getStatusCode();
            String resp = EntityUtils.toString(respuesta.getEntity());
            Log.d("getRequestJson", resp);
            if(code == 200||code == 404)
                return resp;
            else{
                Log.e("Respuesta erronea", resp);
            }

        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e){
            return null;
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            return null;
        }
        return null;

    }

    public static String getRequest(String uri, ArrayList<Header> headers){

        HttpClient httpClient = new DefaultHttpClient(getParams());
        HttpGet getRequest = new HttpGet(uri);
        for(Header h:headers)
            getRequest.addHeader(h);

        getRequest.addHeader("Content-type", "application/json");
        getRequest.addHeader("Accept", "application/json");
        getRequest.addHeader("Authorization", "Basic");

        try {
            HttpResponse respuesta = httpClient.execute(getRequest);
            int code = respuesta.getStatusLine().getStatusCode();
            String resp = EntityUtils.toString(respuesta.getEntity());
            Log.d("getRequestJson", resp);
            if(code == 200)
                return resp;
            else{
                Log.e("Respuesta erronea", resp);
            }

        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e){
            return null;
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            return null;
        }
        return null;

    }

    public static String putRequest(JSONObject json, String uri, ArrayList<Header> headers){

        HttpClient httpClient = new DefaultHttpClient(getParams());
        HttpPut putRequest = new HttpPut(uri);

        for(Header h:headers)
            putRequest.addHeader(h);

        putRequest.addHeader("Content-type", "application/json");
        putRequest.addHeader("Accept", "application/json");
        putRequest.addHeader("Authorization", "Basic");
        try {
            putRequest.setEntity(new StringEntity(json.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        try {
            HttpResponse respuesta = httpClient.execute(putRequest);
            int code = respuesta.getStatusLine().getStatusCode();
            String resp = EntityUtils.toString(respuesta.getEntity());
            if(code == 200 || code == 201|| code == 202|| code == 204)
                return resp;
            else{
                if(code == 400 || code == 401 || code == 406)
                    Log.e("Error", resp);
            }

        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e){
            return null;
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public static String postRequest(JSONObject json, String uri){

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(uri);
        postRequest.addHeader("Content-type", "application/json");
        postRequest.addHeader("Accept", "application/json");
        postRequest.addHeader("Authorization", "Basic");
        try {
            postRequest.setEntity(new StringEntity(json.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        try {
            HttpResponse respuesta = httpClient.execute(postRequest);
            int code = respuesta.getStatusLine().getStatusCode();
            String resp = EntityUtils.toString(respuesta.getEntity());
            if(code == 200 || code == 422) {
                Log.d("respuesta", resp);
                return resp;
            }
            else{
                Log.e("Respuesta erronea", resp);
            }
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e){
            return null;
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            return null;
        }
        return null;
    }

    public static String postRequest(JSONObject json, String uri, String token){

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(uri);
        postRequest.addHeader("Content-type", "application/json");
        postRequest.addHeader("Accept", "application/json");
        postRequest.addHeader("Authorization", "Token token=\"" +token+ "\"");
        try {
            postRequest.setEntity(new StringEntity(json.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        try {
            HttpResponse respuesta = httpClient.execute(postRequest);
            int code = respuesta.getStatusLine().getStatusCode();
            String resp = EntityUtils.toString(respuesta.getEntity());
            if(code == 200 || code == 422) {
                Log.d("respuesta", resp);
                return resp;
            }
            else{
                Log.e("Respuesta erronea", resp);
            }
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e){
            return null;
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            return null;
        }
        return null;
    }

    private static HttpParams getParams(){
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 20000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;
    }

    //metodo para validar correo electronio
    public static boolean isEmail(String correo) {
        Pattern pat = null;
        Matcher mat = null;
        pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            return true;
        }else{
            return false;
        }
    }

    public static boolean isRut(String rutClient){
        if(rutClient.length()>7) {
            String rut = rutClient.substring(0, rutClient.length() - 1);
            String dv = rutClient.substring(rutClient.length() - 1).toUpperCase();
            // Las partes del rut (numero y dv) deben tener una longitud positiva

            // Capturamos error (al convertir un string a entero)
            try {
                int rutInt = Integer.parseInt(rut);
                char dvC = dv.charAt(0);

                // Validamos que sea un rut valido según la norma
                if (validarRut(rutInt, dvC))
                    return true;
                else
                    return false;
            } catch (Exception ex) {
                System.out.println(" Error " + ex.getMessage());
            }
        }

        return false;
    }

    /*
     * Método Estático que valida si un rut es válido
     * Fuente : http://www.creations.cl/2009/01/generador-de-rut-y-validador/
     */
    private static boolean validarRut(int rut, char dv){
        int m = 0, s = 1;
        for (; rut != 0; rut /= 10){
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return dv == (char) (s != 0 ? s + 47 : 75);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String stringToSHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public static boolean isCurrent(String endDate){
        final DateTime newDate = new DateTime();
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        final DateTime dateTimeCoupon = formatter.parseDateTime(endDate);

        if(newDate.isAfter(dateTimeCoupon)) {
            return false;
        }
        else {
            return true;
        }
    }

    public static String getDate(String endDate){
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        final DateTimeFormatter formatter2 = DateTimeFormat.forPattern("dd/MM/yyyy");

        return formatter2.print(formatter.parseDateTime(endDate));
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

}
