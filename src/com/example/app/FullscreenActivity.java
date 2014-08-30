package com.example.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.app.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = false;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;
    private Boolean mLoadComplete = false; //make this false
    Intent mIntent;
    Bundle mSendBundle;
    String mJsonString;
    HttpTask mHttpTask;
    int mHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final ImageView contentView = (ImageView)findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            if(mLoadComplete)
            {
                contentView.setImageResource(R.drawable.trivia_splash_play);
            }
            else
            {
                contentView.setImageResource(R.drawable.trivia_splash_load);
            }
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            if(mLoadComplete)
            {
                contentView.setImageResource(R.drawable.trivia_splash_play_h);
            }
            else
            {
                contentView.setImageResource(R.drawable.trivia_splash_load_h);
            }
        }
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                               // mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            /*controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);*/
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            //controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            mSystemUiHider.hide();
                            //delayedHide(10);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mSystemUiHider.show();
                mSystemUiHider.hide();
                if(mLoadComplete)
                {
                    mSendBundle = new Bundle();
                    mSendBundle.putString("jsonString",mJsonString);
                    mIntent = new Intent(FullscreenActivity.this,QuizActivity.class);
                    mIntent.putExtra("bundle",mSendBundle);
                    startActivity(mIntent);
                    finish();
                }
                //delayedHide(100);
                /*if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }*/
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        //System.out.println(getResources().getConfiguration().orientation);
        loadSharedPrefrences();
        getActionBar().setTitle("High Score: "+mHighScore);

    }
    private void loadSharedPrefrences()
    {
    	SharedPreferences shPref = PreferenceManager.getDefaultSharedPreferences(this);
    	mHighScore = shPref.getInt("highScore", 0);
    	
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHttpTask = new HttpTask();
        //mJsonString = mHttpTask.execute();
        mHttpTask.execute();
        //chkAsyncTaskStatus();
    }

   /* void chkAsyncTaskStatus()
    {
        if(mHttpTask.getStatus() == AsyncTask.Status.RUNNING)
        {
            //chkAsyncTaskStatus();
        }
        else
        {
            mLoadComplete = true;
            final ImageView contentView = (ImageView)findViewById(R.id.fullscreen_content);
            mJsonString = mHttpTask.mJson;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                contentView.setImageResource(R.drawable.trivia_splash_play);
            }
            else
            {
                contentView.setImageResource(R.drawable.trivia_splash_play_h);
            }
        }
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final ImageView contentView = (ImageView)findViewById(R.id.fullscreen_content);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            if(mLoadComplete)
            {
                contentView.setImageResource(R.drawable.trivia_splash_play);
            }
            else
            {
                contentView.setImageResource(R.drawable.trivia_splash_load);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            if(mLoadComplete)
            {
                contentView.setImageResource(R.drawable.trivia_splash_play_h);
            }
            else
            {
                contentView.setImageResource(R.drawable.trivia_splash_load_h);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(100);
        mSystemUiHider.hide();
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                //delayedHide(AUTO_HIDE_DELAY_MILLIS);
                mSystemUiHider.hide();
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    
    @Override
    public void onBackPressed() {
    	finish();
    }
    
    public class HttpTask extends AsyncTask<String, Void, String> {
    	
    	public String mJson;
    	
    	@Override
    	public String doInBackground(String... params)
        {

    		HttpURLConnection con = null ;
            InputStream is = null;
            String url = "https://dl.dropboxusercontent.com/u/8606210/trivia.json";//params[0];
            

            try {
                con = (HttpURLConnection) ( new URL(url)).openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                // Let's read the response
                StringBuffer buffer = new StringBuffer();
                is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while (  (line = br.readLine()) != null )
                    buffer.append(line + "\r\n");

                is.close();
                con.disconnect();
                mJson = buffer.toString();
                return buffer.toString();
            }
            catch(Throwable t) {
                t.printStackTrace();
                return null;
            }
            finally {
                try { is.close(); } catch(Throwable t) {}
                try { con.disconnect(); } catch(Throwable t) {}
            }
    	}
    	
    	@Override
    	protected void onProgressUpdate(Void... values) {
    		// TODO Auto-generated method stub
    		super.onProgressUpdate(values);
    	}
    	
    	@Override
    	protected void onPostExecute(String result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    		mLoadComplete = true;
            final ImageView contentView = (ImageView)findViewById(R.id.fullscreen_content);
            mJsonString = mHttpTask.mJson;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                contentView.setImageResource(R.drawable.trivia_splash_play);
            }
            else
            {
                contentView.setImageResource(R.drawable.trivia_splash_play_h);
            }
    	}

    }

}
