package com.example.plantproject.View;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.plantproject.DataAccessLayer.DatabaseHelper;
import com.example.plantproject.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.hardware.Sensor.TYPE_LIGHT;


public class MonitorActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private SurfaceView qr_scanner;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private String intentData = "";
    private TextView txtBarcodeValue, plantHealth, sensorReadings;
    private TextView min_moist, max_moist, min_temp, max_temp, min_humid, max_humid, min_light, max_light;
    private ImageView profile_pic;
    private View moist_div, temp_div, humid_div, light_div;
    private View prev_moist_div, prev_temp_div, prev_humid_div, prev_light_div;
    private ImageView moist_scale, temp_scale, humid_scale, light_scale;
    private DatabaseHelper mDatabaseHelper;
    private ObjectAnimator moist_translateX, temp_translateX, humid_translateX, light_translateX;
    private ObjectAnimator prev_moist_translateX, prev_temp_translateX, prev_humid_translateX, prev_light_translateX;
    private String p_type;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private int lightValue, prev_lightValue, id_plant;
    private double min_m, max_m, min_t, max_t, min_h, max_h, previous_m, previous_t, previous_h, previous_l;
    private double min_l = 0.0;
    private double max_l = 1614.0;
    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    private BluetoothAdapter bluetoothAdapter;
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private TextToSpeech myTTS;
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private Button button_connect, button_record;


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
        moist_div.setVisibility(View.INVISIBLE);
        temp_div.setVisibility(View.INVISIBLE);
        humid_div.setVisibility(View.INVISIBLE);
        light_div.setVisibility(View.INVISIBLE);
        prev_light_div.setVisibility(View.INVISIBLE);
        moist_scale = findViewById(R.id.moisture_scale);
        temp_scale = findViewById(R.id.temperature_scale);
        humid_scale = findViewById(R.id.humidity_scale);
        light_scale = findViewById(R.id.light_scale);
        plantHealth = findViewById(R.id.plant_health);
        button_connect = findViewById(R.id.button_connect);
        button_record = findViewById(R.id.button_record);
        button_connect.setVisibility(View.INVISIBLE);
        button_record.setVisibility(View.INVISIBLE);
        sensorReadings = findViewById(R.id.sensor_readings);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(TYPE_LIGHT);
        if (lightSensor == null) {
            toastMessage("This device does not have a light sensor");

        }
        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                lightValue = (int) ((((Double.parseDouble(Float.toString(sensorEvent.values[0]))) - min_l) / (max_l - min_l)) * 100);
                int light_deltaX = ((light_scale.getRight() - light_scale.getLeft()) * lightValue / 100) + light_scale.getLeft()
                        - (((light_div.getRight() - light_div.getLeft()) / 2) + light_div.getLeft());
                light_translateX = ObjectAnimator.ofFloat(light_div, "translationX", light_deltaX);
                light_translateX.start();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
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
                    if (ActivityCompat.checkSelfPermission(MonitorActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(qr_scanner.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MonitorActivity.this, new
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
                            if (mDatabaseHelper.checkSingleProfile(intentData)) {
                                Cursor profile = mDatabaseHelper.getSingleProfile(intentData);
                                if (profile.moveToFirst()) {
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
                                        prev_lightValue = (int) previous_l;
                                        int prev_light_deltaX = ((light_scale.getRight() - light_scale.getLeft()) * prev_lightValue / 100) + light_scale.getLeft()
                                                - (((light_div.getRight() - light_div.getLeft()) / 2) + light_div.getLeft());
                                        prev_light_translateX = ObjectAnimator.ofFloat(prev_light_div, "translationX", prev_light_deltaX);
                                        prev_light_translateX.start();

                                    } while (profile.moveToNext());
                                }
                                try {
                                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                                    File directory = cw.getDir("PlantImages", Context.MODE_PRIVATE);
                                    File imagePath = new File(directory, txtBarcodeValue.getText() + ".jpg");
                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(90);
                                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(new FileInputStream(imagePath)), 500, 500, true);
                                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                                    profile_pic.setImageBitmap(rotatedBitmap);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                cameraSource.stop();
                                toastMessage("QR scanner stopped");
                                qr_scanner.setVisibility(View.INVISIBLE);
                                profile_pic.setVisibility(View.VISIBLE);
                                button_connect.setVisibility(View.VISIBLE);

                                prev_light_div.setVisibility(View.VISIBLE);
                            } else toastMessage("QR code does not have a profile");

                        }
                    });
                }
            }
        });
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


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
        sensorManager.unregisterListener(lightEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
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
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {

                // connection to bluetooth
                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1) {
                        //Toast.makeText(getBaseContext(), "Device connected", Toast.LENGTH_SHORT).show();
                        speakWords("Device connected");
                        button_connect.setText("Connected");
                        button_record.setVisibility(View.VISIBLE);
                        moist_div.setVisibility(View.VISIBLE);
                        temp_div.setVisibility(View.VISIBLE);
                        humid_div.setVisibility(View.VISIBLE);
                        light_div.setVisibility(View.VISIBLE);

                    } else {
                        //Toast.makeText(getBaseContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                        button_connect.setText("Reconnect");
                        speakWords("Connection not established");
                        button_record.setVisibility(View.INVISIBLE);

                    }
                }

                if (msg.what == MESSAGE_READ) {
                    byte[] readBuf = (byte[]) msg.obj;
                    String strIncom = new String(readBuf, 0, msg.arg1);
                    sensorReadings.setText(strIncom);
                }
            }
        };
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
                toastMessage("Device has disconnected");
                button_connect.setText("Disconnected");
                button_record.setVisibility(View.INVISIBLE);
                moist_div.setVisibility(View.INVISIBLE);
                temp_div.setVisibility(View.INVISIBLE);
                humid_div.setVisibility(View.INVISIBLE);
                light_div.setVisibility(View.INVISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetConnection();
                        speakWords("Device has disconnected");
                        button_connect.setText("Reconnect");
                    }
                }, 3000);

            }
        }
    };


    private void connect() {
        if (!bluetoothAdapter.isEnabled()) {
            toastMessage("Bluetooth not on");
            return;
        }
        button_connect.setText("Connecting...");
        // Connect to device name and MAC address
        final String name = "Adafruit EZ-Link 8e6a";
        final String address = "98:76:B6:00:8E:6A";
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
            } catch (Exception e) {
            }
            mBTSocket = null;
        }

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //creates secure outgoing connection with BT device using UUID
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }


    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;

            // Get the input stream, using temp objects because
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException ignored) {
            }
            mmInStream = tmpIn;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
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
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            toastMessage("Sorry! Text To Speech failed...");
        }
    }

    public void buttonRecord(View view) {
        toastMessage("Sensor DataActivity Logged");
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String sensorData = dateFormat.format(date) + ", Moisture: "
                + "0," + " Temperature: " + "0," + " Humidity: " + "0," + " Light: " + lightValue + "\n";
        String healthData = "Estimated Health: " + plantHealth.getText() + "\n\n";

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(txtBarcodeValue.getText() + ".txt", MODE_APPEND);
            fos.write(sensorData.getBytes());
            fos.write(healthData.getBytes());
            //Toast.makeText(this, "Saved to "+ getFilesDir() + "/" + txtBarcodeValue.getText()+".txt", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        mDatabaseHelper.editData(id_plant, String.valueOf(txtBarcodeValue.getText()), p_type, min_m, max_m, min_t,
                max_t, min_h, max_h, min_l, max_l, 0.0, 0.0, 0.0, (double) lightValue);
    }

    public void toastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(Color.rgb(36, 100, 36));
        view.setBackground(getResources().getDrawable(R.drawable.btngradient));
        TextView toastMessage = toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toast.show();
    }
}