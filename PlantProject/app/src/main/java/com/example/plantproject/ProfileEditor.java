package com.example.plantproject;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.WriterException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class ProfileEditor extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    
    ImageView qrViewer, qr_background;
    DatabaseHelper mDatabaseHelper;

    private ImageButton imageButton;
    private Camera mCamera;
    private double m_min, m_max, t_min, t_max, h_min, h_max, l_min, l_max;
    private boolean editFlag = false;
    private int id;
    EditText plant_name, moist_min, moist_max, temp_min, temp_max,
            humid_min, humid_max, light_min, light_max;
    String plant_type_text, qr_string;
    Spinner plant_type;
    TextView jpg_name;
    private byte[] pic_data;
    private boolean pic_taken;
    private Map<String, List<String>> plantMap;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();

        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_profile_editor);

        plant_name = findViewById(R.id.plant_name);
        plant_name.setEnabled(true);
        plant_type = findViewById(R.id.plant_type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.plants,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plant_type.setAdapter(adapter);
        plant_type.setBackgroundColor(Color.parseColor("White"));
        plant_type.setOnItemSelectedListener(this);


        moist_min = findViewById(R.id.moisture_min);
        //moist_min.setText("0.0");
        moist_min.setImeOptions(EditorInfo.IME_ACTION_DONE);
        moist_max = findViewById(R.id.moisture_max);
        //moist_max.setText("100.0");
        moist_max.setImeOptions(EditorInfo.IME_ACTION_DONE);
        temp_min = findViewById(R.id.temp_min);
        //temp_min.setText("0.0");
        temp_min.setImeOptions(EditorInfo.IME_ACTION_DONE);
        temp_max = findViewById(R.id.temp_max);
        //temp_max.setText("50.0");
        temp_max.setImeOptions(EditorInfo.IME_ACTION_DONE);
        humid_min = findViewById(R.id.humid_min);
        //humid_min.setText("0.0");
        humid_min.setImeOptions(EditorInfo.IME_ACTION_DONE);
        humid_max = findViewById(R.id.humid_max);
        //humid_max.setText("100.0");
        humid_max.setImeOptions(EditorInfo.IME_ACTION_DONE);
        light_min = findViewById(R.id.light_min);
        //light_min.setText("0.0");
        light_min.setImeOptions(EditorInfo.IME_ACTION_DONE);
        light_max = findViewById(R.id.light_max);
        //light_max.setText("1614.0");
        light_max.setImeOptions(EditorInfo.IME_ACTION_DONE);
        jpg_name = findViewById(R.id.jpg_name);

        plant_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    plant_name.clearFocus();
                } return false;
            }
        });

        moist_min.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    moist_min.clearFocus();
                } return false;
            }
        });

        moist_max.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    moist_max.clearFocus();
                } return false;
            }
        });

        temp_min.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    temp_min.clearFocus();
                } return false;
            }
        });

        temp_max.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    temp_max.clearFocus();
                } return false;
            }
        });

        humid_min.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    humid_min.clearFocus();
                } return false;
            }
        });

        humid_max.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    humid_max.clearFocus();
                } return false;
            }
        });

        light_min.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    light_min.clearFocus();
                } return false;
            }
        });

        light_max.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    light_max.clearFocus();
                } return false;
            }
        });

        Intent incomingData = getIntent();
        editFlag = Objects.requireNonNull(incomingData.getExtras()).getBoolean("edit_flag");
        if(editFlag) {
            id = incomingData.getExtras().getInt("profile_id");
            plant_name.setText(incomingData.getExtras().getString("plant_name"));
            plant_name.setEnabled(false);
            plant_type.setSelection(adapter.getPosition(incomingData.getExtras().getString("plant_type")));
            moist_min.setText(String.valueOf(incomingData.getExtras().getDouble("moist_min")));
            moist_max.setText(String.valueOf(incomingData.getExtras().getDouble("moist_max")));
            temp_min.setText(String.valueOf(incomingData.getExtras().getDouble("temp_min")));
            temp_max.setText(String.valueOf(incomingData.getExtras().getDouble("temp_max")));
            humid_min.setText(String.valueOf(incomingData.getExtras().getDouble("humid_min")));
            humid_max.setText(String.valueOf(incomingData.getExtras().getDouble("humid_max")));
            light_min.setText(String.valueOf(incomingData.getExtras().getDouble("light_min")));
            light_max.setText(String.valueOf(incomingData.getExtras().getDouble("light_max")));
        }

        qrViewer = findViewById(R.id.qr_viewer);
        qr_background = findViewById(R.id.qr_background);

        imageButton = findViewById(R.id.imageButton);

        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        mCamera.getParameters().setRotation(90);
        CameraPreview mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        mDatabaseHelper = new DatabaseHelper(this);

        plantMap = new HashMap<>();

        List<String> Optional = new ArrayList<>();
        Optional.add("0.0");
        Optional.add("100.0");
        Optional.add("0.0");
        Optional.add("50.0");
        Optional.add("0.0");
        Optional.add("100.0");
        Optional.add("0.0");
        Optional.add("100000.0");
        List<String> Low = new ArrayList<>();
        Low.add("0.0");
        Low.add("80.0");
        Low.add("0.0");
        Low.add("40.0");
        Low.add("10.0");
        Low.add("100.0");
        Low.add("100.0");
        Low.add("80000.0");
        List<String> Medium = new ArrayList<>();
        Medium.add("35.0");
        Medium.add("100.0");
        Medium.add("0.0");
        Medium.add("45.0");
        Medium.add("20.0");
        Medium.add("100.0");
        Medium.add("200.0");
        Medium.add("100000.0");
        List<String> High = new ArrayList<>();
        High.add("50.0");
        High.add("100.0");
        High.add("0.0");
        High.add("60.0");
        High.add("30.0");
        High.add("100.0");
        High.add("400.0");
        High.add("120000.0");

        plantMap.put("(Optional)", Optional);
        plantMap.put("Low Tolerance", Low);
        plantMap.put("Medium Tolerance", Medium);
        plantMap.put("High Tolerance", High);


    }

    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance

        } catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable*/
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            pic_data = data;
            pic_taken = true;
            imageButton.setEnabled(false);

        }
    };


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

    public void buttonCancel(View view) {
        mCamera.release();
        Intent intent = new Intent(this, PlantProfiles.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        }
        startActivity(intent);


    }

    @SuppressLint("StaticFieldLeak")
    public void buttonDone(View view) {
        String name = plant_name.getText().toString();
        String plant_ty = plant_type_text;
        String moist_mn = moist_min.getText().toString();
        if(moist_mn.matches("")){
            moist_mn = "0.0";
        }
        m_min = Double.parseDouble(moist_mn);
        String moist_mx = moist_max.getText().toString();
        if(moist_mx.matches("")){
            moist_mx = "100.0";
        }
        m_max = Double.parseDouble(moist_mx);
        String temp_mn = temp_min.getText().toString();
        if(temp_mn.matches("")){
            temp_mn = "0.0";
        }
        t_min = Double.parseDouble(temp_mn);
        String temp_mx = temp_max.getText().toString();
        if(temp_mx.matches("")){
            temp_mx = "40.0";
        }
        t_max = Double.parseDouble(temp_mx);
        String humid_mn = humid_min.getText().toString();
        if(humid_mn.matches("")){
            humid_mn = "0.0";
        }
        h_min = Double.parseDouble(humid_mn);
        String humid_mx = humid_max.getText().toString();
        if(humid_mx.matches("")){
            humid_mx = "100.0";
        }
        h_max = Double.parseDouble(humid_mx);
        String light_mn = light_min.getText().toString();
        if(light_mn.matches("")){
            light_mn = "0.0";
        }
        l_min = Double.parseDouble(light_mn);
        String light_mx = light_max.getText().toString();
        if(light_mx.matches("")){
            light_mx = "100000.0";
        }
        l_max = Double.parseDouble(light_mx);

        double a_moist = 0.0;
        double a_temp = 0.0;
        double a_humid = 0.0;
        double a_light = 0.0;

        if (!editFlag && plant_name.length() != 0 && pic_taken) {
            addData(name, plant_ty, m_min, m_max, t_min, t_max, h_min,
                    h_max, l_min, l_max, a_moist, a_temp, a_humid, a_light);

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("PlantImages", Context.MODE_PRIVATE);
            File imagePath = new File(directory, plant_name.getText() + ".jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imagePath);
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(pic_data, 0, pic_data.length);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert fos != null;
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            mCamera.release();
            pic_taken = false;
            pic_data = null;
            finish();
            Intent intent = new Intent(this, PlantProfiles.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            }
            startActivity(intent);
        }else if(!editFlag && plant_name.length() != 0 && !pic_taken){
            toastMessage("Please take a photo of your plant");

        } else if (editFlag && plant_name.length()!=0 && !pic_taken) {
            editData(id, name, plant_ty, m_min, m_max, t_min, t_max, h_min,
                    h_max, l_min, l_max, a_moist, a_temp, a_humid, a_light);
            mCamera.release();
            finish();
            Intent intent = new Intent(this, PlantProfiles.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            }
            startActivity(intent);
        } else if (editFlag && plant_name.length()!=0 && pic_taken) {
            editData(id, name, plant_ty, m_min, m_max, t_min, t_max, h_min,
                    h_max, l_min, l_max, a_moist, a_temp, a_humid, a_light);

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("PlantImages", Context.MODE_PRIVATE);
            File imagePath = new File(directory, plant_name.getText() + ".jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imagePath);
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(pic_data, 0, pic_data.length);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            mCamera.release();
            pic_taken = false;
            pic_data = null;
            finish();
            Intent intent = new Intent(this, PlantProfiles.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            }
            startActivity(intent);

        } else if (m_min > m_max || m_min < 0 || m_max > 100 && plant_name.length()!=0){
            toastMessage("Please check moisture values");
        } else if (t_min > t_max && plant_name.length()!=0){
            toastMessage("Please check temperature values");
        } else if (h_min > h_max || h_min < 0 || h_max > 100 && plant_name.length()!=0){
            toastMessage("Please check humidity values");
        } else if (l_min > l_max || l_min < 0 && plant_name.length()!=0){
            toastMessage("Please check luminance values");
        } else {
            toastMessage("Please enter a name");
        }


        new AsyncTask<Void, Void, Void>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {    //sync logged sensor information to website

                // request arguments

                String rq = "call usp_insert_PProfile('"+ name +
                        "', '" + plant_ty + "', " + m_min + ","+ m_max +"," + t_min +"," + t_max + "," +
                        h_min + "," + h_max + ", " + l_min + "," + l_max +");";

                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL("https://www.mdlproto.com/PlantifulWeb/Stem/RQStem.php");
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
                    Log.d("Debug", sb.toString());
                } catch (Exception e) {
                    Log.d("Debug", e.getMessage() == null ? "NULL MSG" + e.toString() : e.getMessage());
                } finally {
                    urlConnection.disconnect();
                }

                return null;
            }
        }.execute();







    }

    public void addData(String name, String plant_t, Double mo_min, Double mo_max, Double temp_min,
                        Double temp_max, Double humid_min, Double humid_max, Double light_min, Double light_max,
                        Double act_moist, Double act_temp, Double act_humid, Double act_light) {
        boolean insertData = mDatabaseHelper.addData(name, plant_t, mo_min , mo_max, temp_min,
                temp_max, humid_min, humid_max, light_min, light_max, act_moist, act_temp, act_humid, act_light);
        if (insertData) {
            toastMessage("Profile successfully saved");
        } else {
            toastMessage("Profile not saved. Try again.");
        }

    }

    public void editData(int id, String name, String plant_t, Double mo_min, Double mo_max, Double temp_min,
                         Double temp_max, Double humid_min, Double humid_max, Double light_min, Double light_max,
                         Double act_moist, Double act_temp, Double act_humid, Double act_light) {
        boolean editDat = mDatabaseHelper.editData(id, name, plant_t, mo_min, mo_max, temp_min,
                temp_max, humid_min, humid_max, light_min, light_max, act_moist, act_temp, act_humid, act_light);
        if (editDat) {
            toastMessage("Profile successfully updated");
        } else {
            toastMessage("Profile not updated. Try again.");
        }

    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(Color.rgb(36,100,36));
        view.setBackground(getResources().getDrawable(R.drawable.btngradient));
        TextView toastMessage = toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toast.show();
    }


    public void buttonPhoto(View view) {
        mCamera.takePicture(null, null, mPicture);
        //imageButton.setVisibility(View.INVISIBLE);
    }

    public void buttonGenerate(View view) throws WriterException {
        if(plant_name.length() != 0){
            String qr_name = plant_name.getText().toString();
            QRGEncoder qrgEncoder = new QRGEncoder(qr_name, null, QRGContents.Type.TEXT, 500);
            final Bitmap qrBits = qrgEncoder.getBitmap();
            qrViewer.setImageBitmap(qrBits);
            qr_background.setVisibility(View.INVISIBLE);
            qrViewer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("image/jpeg");
                    String share = "Print this QR code and attach to plant";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Project Plant QR Code");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, share);

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "title");
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);

                    OutputStream outstream;
                    try {
                        assert uri != null;
                        outstream = getContentResolver().openOutputStream(uri);
                        qrBits.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                        assert outstream != null;
                        outstream.close();
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    Cursor returnCursor = getContentResolver().
                            query(uri, null, null, null, null);
                    assert returnCursor !=null;
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    returnCursor.moveToFirst();
                    qr_string = returnCursor.getString(nameIndex);
                    returnCursor.close();
                    return true;
                }
            });

        }else{
            toastMessage("Please enter a name");
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        plant_type_text = adapterView.getItemAtPosition(i).toString();
        List<String> values = plantMap.get(plant_type_text);
        assert values != null;
        moist_min.setText(values.get(0));
        moist_max.setText(values.get(1));
        temp_min.setText(values.get(2));
        temp_max.setText(values.get(3));
        humid_min.setText(values.get(4));
        humid_max.setText(values.get(5));
        light_min.setText(values.get(6));
        light_max.setText(values.get(7));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}