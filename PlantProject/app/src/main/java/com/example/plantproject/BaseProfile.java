package com.example.plantproject;

import android.graphics.Bitmap;

public class BaseProfile {
    private int id;
    private Bitmap image1;
    private int image2;
    private String profile;
    public BaseProfile(int id, Bitmap image1, int image2, String profile){
        this.id = id;
        this.image1 = image1;
        this.image2 = image2;
        this.profile = profile;

    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public Bitmap getImageOpenProfile1(){
        return image1;
    }
    public void setImageOpenProfile1(Bitmap image1){
        this.image1 = image1;
    }
    public int getImageOpenProfile2(){
        return image2;
    }
    public void setImageOpenProfile2(int image2){
        this.image2 = image2;
    }
    public String getProfile(){
        return profile;
    }
    public void setProfile(String profile){
        this.profile = profile;
    }

}
