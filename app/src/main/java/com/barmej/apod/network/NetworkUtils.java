package com.barmej.apod.network;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.barmej.apod.R;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * This utility class will be used to communicate with the weather servers.
 */
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /*
     * Nasa Base API Url
     */
    private static final String BASE_URL = "https://api.nasa.gov/planetary/";

    /**
     * Apod endpoint
     */
    private static final String APOD_ENDPOINT = "apod";


    /* The FORMAT parameter allows us to designate Nasa picture of the day we want JSON or XML from our API */
    private static final String API_KEY_PARAM = "api_key";

    /**
     * Units parameter allows us to designate picture date
     */
    private static final String DATE_PARAM = "date";

    /**
     * HdD parameter
     */
    private static final String HD_PARAM = "hd";

    /**
     * Object used for the purpose of synchronize lock
     */
    private static final Object LOCK = new Object();

    /**
     * Instance of this class for Singleton
     */
    private static NetworkUtils sInstance;

    /**
     * Instance of the application context
     */
    private Context mContext;

    /**
     * Instance of Volley APOD Astronomy picture of the day
     */
    private RequestQueue mRequestQueue;

    /**
     * @param context Context to use for some initializations
     */
    private NetworkUtils(Context context) {
        // getApplicationContext() is key, it keeps your application safe from leaking the
        // Activity or BroadcastReceiver if you pass it instead of application context
        mContext = context.getApplicationContext();

        // Init Volley object
        mRequestQueue = Volley.newRequestQueue( context );
    }

    /**
     * Method used to get an instance of NetworkUtils class
     *
     * @param context Context to use for some initializations
     * @return an instance of NetworkUtils class
     */
    public static synchronized NetworkUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) sInstance = new NetworkUtils( context );
            }
        }
        return sInstance;
    }

    /*
    Builder Url method

     */
    public URL buildUrl(String date) {
        Uri.Builder uriBuilder = Uri.parse( BASE_URL + APOD_ENDPOINT ).buildUpon();
        Uri uri = uriBuilder.appendQueryParameter( API_KEY_PARAM, mContext.getString( R.string.api_key ) )
                .appendQueryParameter( DATE_PARAM, date )
                .appendQueryParameter( HD_PARAM, "true" ).build();

        try {
            URL url = new URL( uri.toString() );
            return url;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
       Add to RequestQueue
     */
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag( tag );
        mRequestQueue.add( request );
    }

    /*
    Tag to cancel requests
    */
    public void cancelRequests(String tag) {
        mRequestQueue.cancelAll( tag );
    }
}