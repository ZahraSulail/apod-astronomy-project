package com.barmej.apod.activities;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.barmej.apod.R;
import com.barmej.apod.data.Astronomy;
import com.barmej.apod.data.DataParser;
import com.barmej.apod.fragment.AboutFragment;
import com.barmej.apod.network.NetworkUtils;
import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    /*
    FragmentManeger
     */
    private FragmentManager mFragmentManager;

    /*
    TouchImageView To view astronomy picture of the day from Nasa website
    */
    private TouchImageView mTouchImageView;

    /*
    WebView to display video from Nasa website
     */
    private WebView mWebView;

    /*
    NetworkUtils Object
     */
    private NetworkUtils networkUtils;

    /*
     TextView to show picture/video title
     */
    private TextView titleTextView;

    /*
     TextView to show information about the  picture/video that displayed
     */
    private TextView descriptionTextView;

    /*
     String variable newData for date format
     */
    private String newDate;

    /*
     ProgressBar is shown while Url Loading
     */
    private ProgressBar mProgressBar;

    /*
      ConstraintLayout object
     */
    private ConstraintLayout constraintLayout;

    /*
     FrameLayout object
     */
    private FrameLayout fragmentContainer;

    /*
      home ConstraintLayot
     */
    private ConstraintLayout homeLayout;

    /*
      BottomSheet is a LinearLayout that contains Two TextViews
      to display title and information about the picture or the video displayed
     */
    private LinearLayout bottomSheet;

    /*
     Astronomy object to get data from Astronomy class
     */
    private Astronomy astronomy;

    /*
     MenuItem object
     */
    private MenuItem downloadMenuItem;

    /*
    zoomOut variable to zoom picture
     */
    private boolean zoomOut = false;

    /*
    nitialize  variables
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        networkUtils = NetworkUtils.getInstance( this );
        mTouchImageView = findViewById( R.id.img_picture_view );
        mWebView = findViewById( R.id.wv_video_player );
        titleTextView = findViewById( R.id.title_text_view );
        descriptionTextView = findViewById( R.id.description_text_view );
        mProgressBar = findViewById( R.id.progressBar );
        homeLayout = findViewById( R.id.home_layout );
        bottomSheet = findViewById( R.id.bottom_sheet );

       /*
       Create Calender object to choose date to show picture/video
        */
        Calendar newCalender = Calendar.getInstance();
        newDate = String.format( Locale.getDefault(), "%d-%d-%d", newCalender.get( Calendar.YEAR ), newCalender.get( Calendar.MONTH ), newCalender.get( Calendar.DAY_OF_MONTH ) );
        requestApod( newDate );
    }

    /*
     requestApod method to GET data from networkUtils as aJsonObjectRequest
     */
    private void requestApod(String date) {
        mProgressBar.setVisibility( View.VISIBLE );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, networkUtils.buildUrl( date ).toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    astronomy = DataParser.parseJson( response );

                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            if (astronomy.getMediaType().equals( "image" )) {
                                mTouchImageView.setVisibility( View.VISIBLE );
                                mWebView.setVisibility( View.GONE );
                                downloadMenuItem.setVisible( true );
                                titleTextView.setText( astronomy.getTitle() );
                                descriptionTextView.setText( astronomy.getExplanation() );
                                Glide.with( mTouchImageView ).load( astronomy.getUrl() ).into( mTouchImageView );

                            } else {
                                mTouchImageView.setVisibility( View.GONE );
                                mWebView.setVisibility( View.VISIBLE );
                                downloadMenuItem.setVisible( false );
                                titleTextView.setText( astronomy.getTitle() );
                                descriptionTextView.setText( astronomy.getExplanation() );
                                mWebView.setWebChromeClient( new WebChromeClient() );
                                mWebView.getSettings().setJavaScriptEnabled( true );
                                mWebView.loadUrl( astronomy.getUrl() );
                            }
                            mProgressBar.setVisibility( View.GONE );
                        }
                    } );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );
        networkUtils.addToRequestQueue( jsonObjectRequest, "apod_request" );
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater
        MenuInflater inflater = getMenuInflater();
        //Use the inflater's inflate method to inflate main_menu layout
        inflater.inflate( R.menu.main_menu, menu );
        downloadMenuItem = menu.findItem( R.id.action_download_hd );
        //Return true to display the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_pick_day) {
            //Open datePicker to select date
            Calendar calendar = Calendar.getInstance();

            int year = calendar.get( Calendar.YEAR );
            int month = calendar.get( Calendar.MONTH );
            int dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH );
            DatePickerDialog datePickerDialog = new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar newCalender = Calendar.getInstance();
                    newCalender.set( year, month, dayOfMonth );

                    newDate = String.format( Locale.getDefault(),"%d-%d-%d", year, month, dayOfMonth );
                    requestApod( newDate );

                }
            }, year, month, dayOfMonth );
            datePickerDialog.show();
            return true;

        } else {

            if (id == R.id.action_about) {
                //Open a dialoge that show short description about the project
                showAboutFragment();
                return true;

            } else {
                if (id == R.id.action_download_hd) {
                    //Download an HD picture or a video
                    downloadHd();
                    return true;

                } else {
                    if (id == R.id.action_share) {
                        //Share picture or video
                        if (astronomy.getMediaType().equals( "image" )) {
                            Intent shareIntent = new Intent( Intent.ACTION_SEND );
                            Uri uri = Uri.parse( astronomy.getUrl() );
                            shareIntent.setType( "image/*" );
                            shareIntent.putExtra( Intent.EXTRA_STREAM, uri );
                            startActivity( shareIntent );
                        } else {
                            Intent shareIntent = new Intent( Intent.ACTION_SEND );
                            shareIntent.setType( "text/*" );
                            shareIntent.putExtra( Intent.EXTRA_TEXT, astronomy.getUrl() );
                            startActivity( shareIntent );
                        }
                        return true;
                    }
                }
                return super.onOptionsItemSelected( item );
            }
        }
    }

    /*
     showAboutFragment method
     */
    private void showAboutFragment() {
        AboutFragment dialogFragment = new AboutFragment();
        dialogFragment.show( getSupportFragmentManager(), "AboutFragment");
    }

    /*
      downLoadHd method to allow user downloading HD image
     */
    private void downloadHd() {

        Uri uri = Uri.parse( astronomy.getUrl() );
        DownloadManager.Request request = new DownloadManager.Request( Uri.parse( String.valueOf( uri ) ) );
        request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI );
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility( DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED );
        request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment() );
        DownloadManager manager = (DownloadManager) getSystemService( Context.DOWNLOAD_SERVICE );
        manager.enqueue( request );

    }
}
