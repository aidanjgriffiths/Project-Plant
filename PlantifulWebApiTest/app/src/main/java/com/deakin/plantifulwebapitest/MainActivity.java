package com.deakin.plantifulwebapitest;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Create an Concurrent task for connecting to the Plantiful Website API
         */
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {

                // request arguments
                String uid = "100";
                String wid = "200";

                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL("http://www.mdlproto.com/Stem/UACStem.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    // write arguments to the output stream of HTTPUrlConnection
                    OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    bufferedWriter.write("uac_w_uid=" + uid + "&uac_w_wid=" + wid);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    urlConnection.connect(); // connect to website and execute HTTP POST request
                    urlConnection.getResponseCode(); // recover the request code to ensure the request did not fail!
                } catch (Exception e) {
                    Log.d("Debug", e.getMessage() == null ? "NULL MSG" + e.toString() : e.getMessage());
                } finally {
                    urlConnection.disconnect();
                }

                return null;
            }
        }.execute();
    }
}