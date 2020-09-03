package com.example.plantproject;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class PlantProfiles extends AppCompatActivity implements ProfileAdapter.OnNoteListener{

    private ArrayList<String> name_profiles = new ArrayList<>();
    private List<BaseProfile> profileList = new ArrayList<>();
    private static final String TAG = "Profile";
    private DatabaseHelper mDatabaseHelper;
    private SwipeController swipeController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();

        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_plant_profiles);

        Button addProfiles = findViewById(R.id.profile_add);

        mDatabaseHelper = new DatabaseHelper(this);
        populateProfiles();

        RecyclerView profileRecyclerView = findViewById(R.id.profile_recycler);
        profileRecyclerView.setHasFixedSize(true);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("PlantImages", Context.MODE_PRIVATE);


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

        ProfileAdapter profileAdapter = new ProfileAdapter(profileList, this);
        profileRecyclerView.setAdapter(profileAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        profileRecyclerView.setLayoutManager(layoutManager);

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

    private void populateProfiles(){
        Log.d(TAG, "Display profiles in ProfileViewer");
        Cursor data = mDatabaseHelper.getData();
        while(data.moveToNext()){
            name_profiles.add(data.getString(1));

        }
    }

    public void buttonBack(View view) {
        Intent intent = new Intent(this, com.example.plantproject.MainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        }
        startActivity(intent);
    }


    public void buttonAdd(View view) {
        Intent intent = new Intent(this, ProfileEditor.class);
        intent.putExtra("edit_flag", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else{
            startActivity(intent);
        }
    }


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


}