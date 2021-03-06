package com.example.plantproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;

public class PlantProfiles extends AppCompatActivity implements ProfileAdapter.OnNoteListener{

    RecyclerView profileRecyclerView;
    ProfileAdapter profileAdapter;
    ArrayList<String> name_profiles = new ArrayList<>();
    List<BaseProfile> profileList = new ArrayList<>();
    Button addProfiles;

    private static final String TAG = "Profile";
    DatabaseHelper mDatabaseHelper;

    SwipeController swipeController = null;

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();

        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_plant_profiles);

        addProfiles = findViewById(R.id.profile_add);

        mDatabaseHelper = new DatabaseHelper(this);
        populateProfiles();

        profileRecyclerView = findViewById(R.id.profile_recycler);
        profileRecyclerView.setHasFixedSize(true);

        new RxPermissions(this)
                .request(Manifest.permission.CAMERA) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (!granted) {
                        toastMessage("Camera permission not granted");
                    }
                });


        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("PlantImages", Context.MODE_PRIVATE);

        // add profile to profile adapter
        for (int i = 0; i < name_profiles.size(); i++) {
            String profile = name_profiles.get(i);
            File imagePath = new File(directory, profile + ".jpg");
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = null;
            try {
                scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(new FileInputStream(imagePath)), 500, 500, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert scaledBitmap != null;
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            com.example.plantproject.BaseProfile profiles =
                    new com.example.plantproject.BaseProfile(i, rotatedBitmap, R.drawable.open_profile, profile);
            profileList.add(profiles);
        }

        // add swipe controller for individual profiles
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                String del_singleProfile = profileList.get(position).getProfile();
                mDatabaseHelper.deleteSingleProfile(del_singleProfile);
                File file = new File(getFilesDir(),del_singleProfile+".txt"); //find the internal stored txt file
                if(file.exists()){
                    deleteFile(del_singleProfile+".txt");  // delete the log .txt file
                }
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("PlantImages", Context.MODE_PRIVATE);
                File image = new File(directory, del_singleProfile+ ".jpg");
                image.delete();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(profileRecyclerView);
        profileRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(canvas);
            }
        });

        profileAdapter = new ProfileAdapter(profileList, this);
        profileRecyclerView.setAdapter(profileAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        profileRecyclerView.setLayoutManager(layoutManager);

    }

    private void populateProfiles(){
        Log.d(TAG, "Display profiles in ProfileViewer");
        Cursor data = mDatabaseHelper.getData();
        while(data.moveToNext()){
            name_profiles.add(data.getString(1));

        }
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    // fullscreen view
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public void buttonBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        }
        startActivity(intent);
    }



    // add a new profile - send to profile editor
    public void buttonAdd(View view) {
        Intent intent = new Intent(this, ProfileEditor.class);
        intent.putExtra("edit_flag", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else{
            startActivity(intent);
        }

    }

    // edit a profile
    @Override
    public void onNoteClick(int position) {
        String profile_edit = profileList.get(position).getProfile();
        Cursor profile = mDatabaseHelper.getSingleProfile(profile_edit);

        Intent intent = new Intent(this, ProfileEditor.class);
        intent.putExtra("edit_flag", true);
        finish();
        if (profile.moveToFirst()){
            do{
                intent.putExtra("profile_id", profile.getInt(0));
                intent.putExtra("plant_name", profile.getString(1));
                intent.putExtra("plant_type", profile.getString(2));
                intent.putExtra("moist_min", profile.getDouble(3));
                intent.putExtra("moist_max", profile.getDouble(4));
                intent.putExtra("temp_min", profile.getDouble(5));
                intent.putExtra("temp_max", profile.getDouble(6));
                intent.putExtra("humid_min", profile.getDouble(7));
                intent.putExtra("humid_max", profile.getDouble(8));
                intent.putExtra("light_min", profile.getDouble(9));
                intent.putExtra("light_max", profile.getDouble(10));

            }while(profile.moveToNext());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        }
        startActivity(intent);
    }

    // themed toast message
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