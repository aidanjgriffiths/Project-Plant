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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String UAC_URL = "https://www.mdlproto.com/PlantifulWeb/Stem/UACStem.php";
        final String RQ_URL = "https://www.mdlproto.com/PlantifulWeb/Stem/RQStem.php";

        /**
         * Create an Concurrent task for connecting to the Plantiful Website API
         */
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                // request arguments
                String uid = "3000";
                String wid = "200";
                String rq = "SELECT * FROM PUser";

                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(UAC_URL);
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
                    int responseCode = urlConnection.getResponseCode(); // recover the request code to ensure the request did not fail!

                    // get response from server
                    StringBuilder sb = new StringBuilder();
                    InputStream is = responseCode == 200 ? urlConnection.getInputStream() : urlConnection.getErrorStream();
                    BufferedReader bis = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String curLine = "";

                    // retrieve each line from the response from the server
                    while ((curLine = bis.readLine()) != null)
                        sb.append(curLine + "\n");

                    // close input streams
                    is.close();
                    bis.close();

                    sb.toString();
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