package com.example.plantproject;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.hardware.Sensor.TYPE_LIGHT;


public class Monitor extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private SurfaceView qr_scanner;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private String intentData = "";
    private TextView txtBarcodeValue, plantHealth;
    private TextView tempdebug, humiddebug, moistdebug, lightdebug;
    private TextView min_moist, max_moist, min_temp, max_temp, min_humid, max_humid, min_light, max_light;
    private ImageView profile_pic;
    private View moist_div, temp_div, humid_div, light_div;
    private View prev_moist_div, prev_temp_div, prev_humid_div, prev_light_div;
    private ImageView moist_scale, temp_scale, humid_scale, light_scale;
    private DatabaseHelper mDatabaseHelper;
    private ObjectAnimator moist_translateX, temp_translateX, humid_translateX, light_translateX;
    private ObjectAnimator prev_moist_translateX, prev_temp_translateX, prev_humid_translateX, prev_light_translateX;
    private String p_type;
    //private SensorManager sensorManager;
    //private Sensor lightSensor;
    //private SensorEventListener lightEventListener;
    private int moistValue, tempValue, humidValue, lightValue, prev_moistValue, prev_tempValue, prev_humidValue, prev_lightValue, id_plant;
    private int moistValue_, tempValue_, humidValue_, lightValue_;
    private double min_m, max_m, min_t, max_t, min_h, max_h, min_l, max_l, previous_m, previous_t, previous_h, previous_l;

    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    private BluetoothAdapter bluetoothAdapter;
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private TextToSpeech myTTS;
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private Button button_connect, button_record, read_sensors;
    private String strIncom;
    private ArrayList<String> ar = new ArrayList<>();
    private ArrayList<String> ar_saved = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();

        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_monitor);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mDatabaseHelper = new DatabaseHelper(this);

        qr_scanner = findViewById(R.id.qr_scanner);
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        min_moist = findViewById(R.id.min_moist);
        max_moist = findViewById(R.id.max_moist);
        min_temp = findViewById(R.id.min_temp);
        max_temp = findViewById(R.id.max_temp);
        min_humid = findViewById(R.id.min_humid);
        max_humid = findViewById(R.id.max_humid);
        min_light = findViewById(R.id.min_light);
        max_light = findViewById(R.id.max_light);
        profile_pic = findViewById(R.id.profile_pic);
        moist_div = findViewById(R.id.moist_div);
        temp_div = findViewById(R.id.temp_div);
        humid_div = findViewById(R.id.humid_div);
        light_div = findViewById(R.id.light_div);
        prev_light_div = findViewById(R.id.prev_light_div);
        prev_humid_div = findViewById(R.id.prev_humid_div);
        prev_temp_div = findViewById(R.id.prev_temp_div);
        prev_moist_div = findViewById(R.id.prev_moist_div);
        moist_div.setVisibility(View.INVISIBLE);
        temp_div.setVisibility(View.INVISIBLE);
        humid_div.setVisibility(View.INVISIBLE);
        light_div.setVisibility(View.INVISIBLE);
        prev_light_div.setVisibility(View.INVISIBLE);
        prev_humid_div.setVisibility(View.INVISIBLE);
        prev_temp_div.setVisibility(View.INVISIBLE);
        prev_moist_div.setVisibility(View.INVISIBLE);
        moist_scale = findViewById(R.id.moisture_scale);
        temp_scale = findViewById(R.id.temperature_scale);
        humid_scale = findViewById(R.id.humidity_scale);
        light_scale = findViewById(R.id.light_scale);
        plantHealth = findViewById(R.id.plant_health);
        button_connect = findViewById(R.id.button_connect);
        button_record = findViewById(R.id.button_record);
        button_connect.setVisibility(View.INVISIBLE);
        button_record.setVisibility(View.INVISIBLE);
        read_sensors = findViewById(R.id.read_sensors);
        read_sensors.setVisibility(View.INVISIBLE);
        tempdebug = findViewById(R.id.tempdebug);
        humiddebug = findViewById(R.id.humiddebug);
        moistdebug = findViewById(R.id.moistdebug);
        lightdebug = findViewById(R.id.lightdebug);

        /*sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(TYPE_LIGHT);
        if (lightSensor == null){
            toastMessage("This device does not have a light sensor");

        }
        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                lightValue = (int)((((Double.parseDouble(Float.toString(sensorEvent.values[0])))-min_l)/(max_l-min_l))*100);
                int light_deltaX = ((light_scale.getRight()-light_scale.getLeft())*lightValue/100)+light_scale.getLeft()
                        -(((light_div.getRight()-light_div.getLeft())/2)+light_div.getLeft());
                light_translateX = ObjectAnimator.ofFloat(light_div, "translationX", light_deltaX);
                light_translateX.start();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };*/
    }


    private void initialiseDetectorsAndSources() {

        //toastMessage("QR code scanner started");
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(960, 540) //1920, 1080
                .setAutoFocusEnabled(true)
                .build();

        qr_scanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Monitor.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(qr_scanner.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(Monitor.this, new
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
                    txtBarcodeValue.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            intentData = barcodes.valueAt(0).displayValue;
                            if(mDatabaseHelper.checkSingleProfile(intentData)){
                                Cursor profile = mDatabaseHelper.getSingleProfile(intentData);
                                if(profile.moveToFirst()) {
                                    do {
                                        id_plant = profile.getInt(0);
                                        txtBarcodeValue.setText(profile.getString(1)); // display profile name
                                        p_type = profile.getString(2);
                                        min_m = profile.getDouble(3);
                                        min_moist.setText(String.valueOf(min_m));
                                        max_m = profile.getDouble(4);
                                        max_moist.setText(String.valueOf(max_m));
                                        min_t = profile.getDouble(5);
                                        min_temp.setText(String.valueOf(min_t));
                                        max_t = profile.getDouble(6);
                                        max_temp.setText(String.valueOf(max_t));
                                        min_h = profile.getDouble(7);
                                        min_humid.setText(String.valueOf(min_h));
                                        max_h = profile.getDouble(8);
                                        max_humid.setText(String.valueOf(max_h));
                                        min_l = profile.getDouble(9);
                                        min_light.setText(String.valueOf(min_l));
                                        max_l = profile.getDouble(10);
                                        max_light.setText(String.valueOf(max_l));
                                        previous_m = profile.getDouble(11);
                                        previous_t = profile.getDouble(12);
                                        previous_h = profile.getDouble(13);
                                        previous_l = profile.getDouble(14);


                                        prev_moistValue = (int)(((previous_m-min_m)/(max_m-min_m))*100);
                                        if(prev_moistValue<0)prev_moistValue = 0;
                                        if(prev_moistValue>100)prev_moistValue = 100;
                                        int prev_moist_deltaX = ((moist_scale.getRight()-moist_scale.getLeft())*prev_moistValue/100)+moist_scale.getLeft()
                                                -(((moist_div.getRight()-moist_div.getLeft())/2)+moist_div.getLeft());
                                        //if(prev_moist_deltaX > (moist_div.getRight()-moist_div.getLeft())/2) prev_moist_deltaX = (moist_div.getRight()-moist_div.getLeft())/2;
                                        prev_moist_translateX = ObjectAnimator.ofFloat(prev_moist_div, "translationX", prev_moist_deltaX);
                                        prev_moist_translateX.start();

                                        prev_tempValue = (int)(((previous_t-min_t)/(max_t-min_t))*100);
                                        if(prev_tempValue<0)prev_tempValue = 0;
                                        if(prev_tempValue>100)prev_tempValue = 100;
                                        int prev_temp_deltaX = ((temp_scale.getRight()-temp_scale.getLeft())*prev_tempValue/100)+temp_scale.getLeft()
                                                -(((temp_div.getRight()-temp_div.getLeft())/2)+temp_div.getLeft());
                                        //if(prev_temp_deltaX > (temp_div.getRight()-temp_div.getLeft())/2) prev_temp_deltaX = (temp_div.getRight()-temp_div.getLeft())/2;
                                        prev_temp_translateX = ObjectAnimator.ofFloat(prev_temp_div, "translationX", prev_temp_deltaX);
                                        prev_temp_translateX.start();

                                        prev_humidValue = (int)(((previous_h-min_h)/(max_h-min_h))*100);
                                        if(prev_humidValue<0)prev_humidValue = 0;
                                        if(prev_humidValue>100)prev_humidValue = 100;
                                        int prev_humid_deltaX = ((humid_scale.getRight()-humid_scale.getLeft())*prev_humidValue/100)+humid_scale.getLeft()
                                                -(((humid_div.getRight()-humid_div.getLeft())/2)+humid_div.getLeft());
                                        //if(prev_humid_deltaX > (humid_div.getRight()-humid_div.getLeft())/2) prev_humid_deltaX = (humid_div.getRight()-humid_div.getLeft())/2;
                                        prev_humid_translateX = ObjectAnimator.ofFloat(prev_humid_div, "translationX", prev_humid_deltaX);
                                        prev_humid_translateX.start();

                                        prev_lightValue = (int)(((previous_l-min_l)/(max_l-min_l))*100);
                                        if(prev_lightValue<0)prev_lightValue = 0;
                                        if(prev_lightValue>100)prev_lightValue = 100;
                                        int prev_light_deltaX = ((light_scale.getRight()-light_scale.getLeft())*prev_lightValue/100)+light_scale.getLeft()
                                                -(((light_div.getRight()-light_div.getLeft())/2)+light_div.getLeft());
                                        //if(prev_light_deltaX > (light_div.getRight()-light_div.getLeft())/2) prev_light_deltaX = (light_div.getRight()-light_div.getLeft())/2;
                                        prev_light_translateX = ObjectAnimator.ofFloat(prev_light_div, "translationX", prev_light_deltaX);
                                        prev_light_translateX.start();

                                    } while (profile.moveToNext());
                                }
                                try{
                                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                                    File directory = cw.getDir("PlantImages", Context.MODE_PRIVATE);
                                    File imagePath = new File(directory, txtBarcodeValue.getText() + ".jpg");
                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(90);
                                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(new FileInputStream(imagePath)), 500, 500, true);
                                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                                    profile_pic.setImageBitmap(rotatedBitmap);
                                }catch (FileNotFoundException e){
                                    e.printStackTrace();
                                }

                                cameraSource.stop();
                                toastMessage("QR scanner stopped");
                                qr_scanner.setVisibility(View.INVISIBLE);
                                profile_pic.setVisibility(View.VISIBLE);
                                button_connect.setVisibility(View.VISIBLE);
                                prev_light_div.setVisibility(View.VISIBLE);
                                prev_temp_div.setVisibility(View.VISIBLE);
                                prev_moist_div.setVisibility(View.VISIBLE);
                                prev_humid_div.setVisibility(View.VISIBLE);

                            } else toastMessage("QR code does not have a profile");
                        }
                    });
                }
            }
        });
    }
    //full screen mode
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


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
        //sensorManager.unregisterListener(lightEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
        //sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void buttonBack(View view) {
        resetConnection();
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        }

    }

    @SuppressLint("HandlerLeak")
    public void buttonConnect(View view) {

        // text-to-speech actions added to intent
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 0);


        IntentFilter filter = new IntentFilter();
        // bluetooth actions added to intent filter
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        this.registerReceiver(myReceiver, filter);  // register the receiver

        connect(); //connect to the device via bluetooth

        //handle connections and read data
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // connection to bluetooth
                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1) {
                        //Toast.makeText(getBaseContext(), "Device connected", Toast.LENGTH_SHORT).show();
                        speakWords("Device connected");
                        button_connect.setText(R.string.connected);
                        read_sensors.setVisibility(View.VISIBLE);
                        plantHealth.setText("");

                    } else {
                        //Toast.makeText(getBaseContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                        button_connect.setText(R.string.reconnect);
                        speakWords("Connection not established");
                        button_record.setVisibility(View.INVISIBLE);

                    }
                }

                if (msg.what == MESSAGE_READ) {
                    byte[] readBuf = (byte[]) msg.obj;
                    strIncom = new String(readBuf, 0, msg.arg1);
                    strIncom = strIncom.replace("\r", "");
                    strIncom = strIncom.replace("\n", "");

                    //splice the Serial data
                    plantHealth.setText(strIncom);
                    String[] strings = strIncom.split("T");
                    String[] strings_ = strings[1].split("H");
                    ar.add(strings_[0]);
                    tempdebug.setText(strings_[0]);
                    String[] strings__ = strings_[1].split("L");
                    ar.add(strings__[0]);
                    humiddebug.setText(strings__[0]);
                    String[] strings___ = strings__[1].split("M");
                    ar.add(strings___[0]);
                    lightdebug.setText(strings___[0]);
                    ar.add(strings___[1]);
                    moistdebug.setText(strings___[1]);
                    //tempdebug.setText(ar.get(0));
                    //humiddebug.setText(ar.get(1));
                    //lightdebug.setText(ar.get(2));
                    //moistdebug.setText(ar.get(3));

                    // move the sliders
                    moistValue = (int) ((((Double.parseDouble(ar.get(3))) - min_m) / (max_m - min_m)) * 100);
                    moistValue_ = moistValue;
                    if (moistValue < 0) moistValue_ = 0;
                    if (moistValue > 100) moistValue_ = 100;
                    int moist_deltaX = ((moist_scale.getRight() - moist_scale.getLeft()) * moistValue_ / 100) + moist_scale.getLeft()
                            - (((moist_div.getRight() - moist_div.getLeft()) / 2) + moist_div.getLeft());
                    moist_translateX = ObjectAnimator.ofFloat(moist_div, "translationX", moist_deltaX);
                    moist_translateX.start();

                    tempValue = (int) ((((Double.parseDouble(ar.get(0))) - min_t) / (max_t - min_t)) * 100);
                    tempValue_ = tempValue;
                    if (tempValue < 0) tempValue_ = 0;
                    if (tempValue > 100) tempValue_ = 100;
                    int temp_deltaX = ((temp_scale.getRight() - temp_scale.getLeft()) * tempValue_ / 100) + temp_scale.getLeft()
                            - (((temp_div.getRight() - temp_div.getLeft()) / 2) + temp_div.getLeft());
                    temp_translateX = ObjectAnimator.ofFloat(temp_div, "translationX", temp_deltaX);
                    temp_translateX.start();

                    humidValue = (int) ((((Double.parseDouble(ar.get(1))) - min_h) / (max_h - min_h)) * 100);
                    humidValue_ = humidValue;
                    if (humidValue < 0) humidValue_ = 0;
                    if (humidValue > 100) humidValue_ = 100;
                    int humid_deltaX = ((humid_scale.getRight() - humid_scale.getLeft()) * humidValue_ / 100) + humid_scale.getLeft()
                            - (((humid_div.getRight() - humid_div.getLeft()) / 2) + humid_div.getLeft());
                    humid_translateX = ObjectAnimator.ofFloat(humid_div, "translationX", humid_deltaX);
                    humid_translateX.start();

                    lightValue = (int) ((((Double.parseDouble(ar.get(2))) - min_l) / (max_l - min_l)) * 100);
                    lightValue_ = lightValue;
                    if (lightValue < 0) lightValue_ = 0;
                    if (lightValue > 100) lightValue_ = 100;
                    int light_deltaX = ((light_scale.getRight() - light_scale.getLeft()) * lightValue_ / 100) + light_scale.getLeft()
                            - (((light_div.getRight() - light_div.getLeft()) / 2) + light_div.getLeft());
                    light_translateX = ObjectAnimator.ofFloat(light_div, "translationX", light_deltaX);
                    light_translateX.start();
                    moist_div.setVisibility(View.VISIBLE);
                    temp_div.setVisibility(View.VISIBLE);
                    humid_div.setVisibility(View.VISIBLE);
                    light_div.setVisibility(View.VISIBLE);
                    button_record.setVisibility(View.VISIBLE);
                    read_sensors.setVisibility(View.INVISIBLE);
                    ar_saved.add(ar.get(0));
                    ar_saved.add(ar.get(1));
                    ar_saved.add(ar.get(2));
                    ar_saved.add(ar.get(3));
                    //arsize.setText(String.valueOf(ar_saved.size()));
                    if (ar_saved.size() > 4) {
                        ar_saved.remove(0);
                        ar_saved.remove(0);
                        ar_saved.remove(0);
                        ar_saved.remove(0);
                        //arsize.setText(String.valueOf(ar_saved.size()));
                    }
                    ar.clear();
                    mConnectedThread.write("r");
                }
            }
        };
        if (bluetoothAdapter != null) {
            read_sensors.setOnClickListener(v -> {
                if(mConnectedThread != null) //First check to make sure thread created
                    mConnectedThread.write("r");
            });
        }

    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                toastMessage("Check bluetooth connection");
                speakWords("Check bluetooth connection");
            }

            // handle disconnect from bluetooth device
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //toastMessage("Device has disconnected");
                speakWords("Device has disconnected");
                button_connect.setText(R.string.disconnected);
                button_record.setVisibility(View.INVISIBLE);
                //moist_div.setVisibility(View.INVISIBLE);
                //temp_div.setVisibility(View.INVISIBLE);
                //humid_div.setVisibility(View.INVISIBLE);
                //light_div.setVisibility(View.INVISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetConnection();
                        //speakWords("Device has disconnected");
                        button_connect.setText(R.string.reconnect);

                    }
                },3000);
                //plantHealth.setText("");
            }
        }
    };

    //establish bluetooth connection and handle exceptions
    private void connect(){
        if(!bluetoothAdapter.isEnabled()) {
            toastMessage("Bluetooth not on");
            return;
        }
        button_connect.setText(R.string.connecting);
        // Connect to device name and MAC address
        final String name = "Adafruit EZ-Link 8e6a";//"HC-05";//"Adafruit EZ-Link 8e6a";
        final String address = "98:76:B6:00:8E:6A";//"98:D3:A1:FD:5C:B6";//"98:76:B6:00:8E:6A";
        new Thread() {
            public void run() {
                boolean fail = false;
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                try {
                    mBTSocket = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;
                    toastMessage("Socket creation failed");
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
                        mBTSocket.close();
                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                .sendToTarget();
                    } catch (IOException e2) {
                        //Toast to indicate socket connection has failed
                        toastMessage("Socket creation failed");
                    }
                }
                if (!fail) {
                    mConnectedThread = new ConnectedThread(mBTSocket);
                    mConnectedThread.start();
                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                            .sendToTarget();
                }
            }
        }.start();
    }
    // reset the bluetooth connection
    private void resetConnection() {
        if (mBTSocket != null) {
            try {
                mBTSocket.close();
            } catch (Exception e) {}
            mBTSocket = null;
        }

    }
    // setup bluetooth socket
    private BluetoothSocket createBluetoothSocket (BluetoothDevice device) throws IOException {
        //creates secure outgoing connection with BT device using UUID
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }

    //setup bluetooth connection thread
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input stream, using temp objects because
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[30];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(300); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }
    }

    // speech synthesis
    private void speakWords(String speech) {
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
    // text-to-speech initiation
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            toastMessage("Sorry! Text To Speech failed...");
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void buttonRecord(View view){        //appends the sensor data to profile specific txt file
        resetConnection();
        //toastMessage("Sensor Data Logged");
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String sensorData = dateFormat.format(date) + ", Moisture: "
                + ar_saved.get(ar_saved.size()-1) + "," +" Temperature: " + ar_saved.get(ar_saved.size()-4)+ "," +" Humidity: "+ ar_saved.get(ar_saved.size()-3) + "," +" Light: " + ar_saved.get(ar_saved.size()-2) + "\n";
        String healthData = "Estimated Plant Health: "+ plantHealth.getText()+"\n\n";

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(txtBarcodeValue.getText()+".txt", MODE_APPEND);
            fos.write(sensorData.getBytes());
            fos.write(healthData.getBytes());
            //Toast.makeText(this, "Saved to "+ getFilesDir() + "/" + txtBarcodeValue.getText()+".txt", Toast.LENGTH_LONG).show();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos!= null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        // normalized current values
        double mse_c= (Math.pow(((Double.parseDouble(ar_saved.get(ar_saved.size()-1))-min_m)/(max_m-min_m))-0.5,2)+
                Math.pow(((Double.parseDouble(ar_saved.get(ar_saved.size()-4))-min_t)/(max_t-min_t))-0.5,2)+
                Math.pow(((Double.parseDouble(ar_saved.get(ar_saved.size()-3))-min_h)/(max_h-min_h))-0.5,2)+
                Math.pow(((Double.parseDouble(ar_saved.get(ar_saved.size()-2))-min_l)/(max_l-min_l))-0.5,2))/4.0;


        // normalized previous values
        double mse_p = (Math.pow(((previous_m-min_m)/(max_m-min_m))-0.5, 2.0) +
                Math.pow(((previous_t-min_t)/(max_t-min_t))-0.5, 2.0)+
                Math.pow(((previous_h-min_h)/(max_h-min_h))-0.5, 2.0)+
                Math.pow(((previous_l-min_l)/(max_l-min_l))-0.5, 2.0))/4.0;

        // display growing condition states
        if(mse_c < 0.02 && mse_c < mse_p) plantHealth.setText("Plant growing conditions are optimal");
        else if(mse_c < 0.02 && mse_c > mse_p) plantHealth.setText("Plant growing conditions are optimal but declining. Check back soon!");
        else if(mse_c >= 0.02 && mse_c < 0.06 && mse_c < mse_p) plantHealth.setText("Plant growing conditions are good and improving");
        else if(mse_c >= 0.02 && mse_c < 0.06 && mse_c > mse_p) plantHealth.setText("Plant growing conditions are good but declining. Check back soon!");
        else if(mse_c >= 0.06 && mse_c < 0.1 && mse_c < mse_p) plantHealth.setText("Plant growing conditions are reasonable and improving");
        else if(mse_c >= 0.06 && mse_c < 0.1 && mse_c > mse_p) plantHealth.setText("Plant growing conditions are reasonable but declining. Action required!");
        else if(mse_c >= 0.1 && mse_c < mse_p) plantHealth.setText("Plant growing conditions are far from optimal but improving");
        else if(mse_c >= 0.1 && mse_c > mse_p) plantHealth.setText("Plant growing conditions are far from optimal and declining. Action required!");

        mDatabaseHelper.editData(id_plant, String.valueOf(txtBarcodeValue.getText()), p_type, min_m, max_m, min_t,
                max_t, min_h, max_h, min_l, max_l, Double.parseDouble(ar_saved.get(ar_saved.size()-1)),
                Double.parseDouble(ar_saved.get(ar_saved.size()-4)), Double.parseDouble(ar_saved.get(ar_saved.size()-3)),
                Double.parseDouble(ar_saved.get(ar_saved.size()-2)));


        new AsyncTask<Void, Void, Void>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {    //sync logged sensor information to website

                // request arguments
                String rq = "SELECT * FROM + TABLE_NAME +";
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL("https://www.mdlproto.com/PlantifulWeb/Stem/UACStem.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    // write arguments to the output stream of HTTPUrlConnection
                    OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    bufferedWriter.write("r_q_str=" + rq);
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

    }

    public void toastMessage(String message) {     //themed toast message function
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(Color.rgb(36,100,36));
        view.setBackground(getResources().getDrawable(R.drawable.btngradient));
        TextView toastMessage = toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toast.show();
    }
}