package com.example.plantproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

public class Data extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private String intentData = "";
    private TextView debugConnection;
    private SurfaceView qr_scan;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();

        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_data);
        mDatabaseHelper = new DatabaseHelper(this);
        qr_scan = findViewById(R.id.webconnect);
        debugConnection = findViewById(R.id.debug);
        initialiseDetectorsAndSources();

    }
    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume(){
        super.onResume();
        initialiseDetectorsAndSources();

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

    private void initialiseDetectorsAndSources() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(960, 540) //1920, 1080
                .setAutoFocusEnabled(true)
                .build();

        qr_scan.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Data.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(qr_scan.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(Data.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //toastMessage("QR scanner stopped");
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    debugConnection.post(new Runnable() {
                        @SuppressLint("StaticFieldLeak")
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            intentData = barcodes.valueAt(0).displayValue;
                            cameraSource.stop();
                            new AsyncTask<Void, Void, Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                protected Void doInBackground(Void... voids) {

                                    // request arguments
                                    String uid = "100";
                                    String wid = intentData;
                                    HttpURLConnection urlConnection = null;

                                    try {
                                        URL url = new URL("https://www.mdlproto.com/PlantifulWeb/Stem/UACStem.php");
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
                                        int responseCode =  urlConnection.getResponseCode(); // recover the request code to ensure the request did not fail!
                                        //debugConnection.setText(responseCode);
                                        Log.d("Debug", String.valueOf(responseCode));
                                    } catch (Exception e) {
                                        Log.d("Debug", e.getMessage() == null ? "NULL MSG" + e.toString() : e.getMessage());

                                    } finally {
                                        urlConnection.disconnect();
                                    }

                                        return null;
                                }
                            }.execute();
                            //cameraSource.stop();
                            toastMessage("QR scanner stopped");

                        }
                    });
                }
            }
        });
    }


    public void buttonReset(View view) {
        CustomResetData cd = new CustomResetData(Data.this);
        cd.setDialogResult(new CustomResetData.OnMyDialogResult(){
            public void finish(String result){
                if(result == "Delete"){
                    String db = mDatabaseHelper.getDatabaseName();
                    getApplicationContext().deleteDatabase(db);
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getDir("PlantImages", Context.MODE_PRIVATE);
                    if(directory.exists()){
                        File[] files = directory.listFiles();
                        assert files != null;
                        for (File file : files) {
                            file.delete();
                        }
                    }


                    File dir = new File(getFilesDir().getPath()); //find the internal stored txt file
                    if(dir.isDirectory()){
                        String[] children = dir.list();
                        for (String child : children) {
                            new File(dir, child).delete();
                        }

                    }
                }
            }
        });
        cd.show();

    }

    public void buttonExport(View view) {
        File dir = new File(getFilesDir().getPath()); //find the internal stored txt file directory
        new File(dir, "Export_Data.txt").delete();
        File direc = new File(getFilesDir().getPath()); //find the internal stored txt file directory
        if (direc.isDirectory()) {

            String[] children = direc.list();
            if(children.length != 0) {
                for (int i = 0; i < children.length; i++) {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(children[i]);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader((isr));
                        StringBuilder sb = new StringBuilder();
                        String text;
                        sb.append("Plant: " + children[i].substring(0, children[i].length() - 4)).append("\n");
                        while ((text = br.readLine()) != null) {      // read the stored .txt file
                            sb.append(text).append("\n");
                        }

                        FileOutputStream fos = null;
                        try {
                            fos = openFileOutput("Export_Data.txt", MODE_APPEND);
                            fos.write(sb.toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close();
                                ;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                Uri path = FileProvider.getUriForFile(this, "com.example.plantproject", new File(getFilesDir() + "/" + "Export_Data.txt"));
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("plain/*");
                String share = "Logged plant data";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "PLANTIFUL - data export");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, share);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, path);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }else toastMessage("No data to export");
        }
    }

    public void buttonBack(View view) {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        }
    }

    public void toastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(Color.rgb(36,100,36));
        view.setBackground(getResources().getDrawable(R.drawable.btngradient));
        TextView toastMessage = toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toast.show();
    }

}