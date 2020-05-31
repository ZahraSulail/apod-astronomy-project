package com.barmej.apod.data;

import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {
    /*
    URL information
     */
    private static final String APOD_COPYRIGHT = "copyright";
    private static final String APOD_DATE = "date";
    private static final String APOD_EXPLANATION = "explanation";
    private static final String APOD_HD_URL = "hdurl";
    private static final String APOD_MEDIA_TYPE = "media_type";
    private static final String APOD_SERVICE_VERSION = "service_version";
    private static final String APOD_TITLE = "title";
    private static final String APOD_URL = "url";

    /*
      Parse Json data
     */
    public static Astronomy parseJson(JSONObject response) throws JSONException {
        Astronomy astronomy = new Astronomy();
        if (response.has( APOD_HD_URL )) {
            astronomy.setHdurl( response.getString( APOD_HD_URL ) );
        }
        if (response.has( APOD_COPYRIGHT )) {
            astronomy.setCopyright( response.getString( APOD_COPYRIGHT ) );
        }
        astronomy.setDate( response.getString( APOD_DATE ) );

        astronomy.setExplanation( response.getString( APOD_EXPLANATION ) );
        astronomy.setMediaType( response.getString( APOD_MEDIA_TYPE ) );
        astronomy.setServiceVersion( response.getString( APOD_SERVICE_VERSION ) );
        astronomy.setTitle( response.getString( APOD_TITLE ) );
        astronomy.setUrl( response.getString( APOD_URL ) );
        return astronomy;
    }
}
