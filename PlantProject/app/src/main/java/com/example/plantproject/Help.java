package com.example.plantproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Help extends AppCompatActivity {

    //private VideoView vid1, vid2, vid3;
    private TextView addProfileHelp, connectingHelp, monitoringHelp;
    private MediaController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();

        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_help);

        //vid1 = findViewById(R.id.how_connect);
        //vid2 = findViewById(R.id.how_addprofile);
        //vid3 = findViewById(R.id.how_monitor);
        addProfileHelp = findViewById(R.id.adding_plant_help);
        connectingHelp = findViewById(R.id.connecting_help);
        monitoringHelp = findViewById(R.id.monitoring_help);

        addProfileHelp.setText("To add a new plant profile, click profile on the home screen, click +add, type in a plant name, " +
                "select a plant profile if the general growing conditions of the plant are known, " +
                "generate a QR code and long press to share. Please take a photo before saving. " +
                "For expert users, the conditional min/max values may be adjusted to suit local conditions.");
        connectingHelp.setText("To connect to the Plantiful device, click monitor on the home screen, scan a plant's QR code and " +
                "press connect at the top of the screen. A text-to-speech prompt notifies you of connections and disconnections.");
        monitoringHelp.setText("To monitor a plant, scan a plant's QR code, connect to the device and press the read sensor data " +
                "button to record the current environmental parameters. Plant health is determined from the difference between " +
                "current and previous sensor readings.");

        mController = new MediaController(this);

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    /*public void playVideo1(View v) {
        String path = "android.resource://" + getPackageName() + "/" + R.raw.vid1;
        Uri u = Uri.parse((path));
        vid1.setVideoURI(u);
        vid1.setMediaController(mController);
        mController.setAnchorView(vid1);

    }

    public void playVideo2(View v) {
        String path = "android.resource://" + getPackageName() + "/" + R.raw.vid1;
        Uri u = Uri.parse((path));
        vid2.setVideoURI(u);
        vid2.setMediaController(mController);
        mController.setAnchorView(vid2);

    }

    public void playVideo3(View v) {
        String path = "android.resource://" + getPackageName() + "/" + R.raw.vid1;
        Uri u = Uri.parse((path));
        vid3.setVideoURI(u);
        vid3.setMediaController(mController);
        mController.setAnchorView(vid3);

    }*/

    public void buttonBack(View view) {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        }
    }
}