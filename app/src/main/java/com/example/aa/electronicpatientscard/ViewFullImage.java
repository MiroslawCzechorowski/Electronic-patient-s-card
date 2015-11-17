package com.example.aa.electronicpatientscard;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.widget.ImageView;

public class ViewFullImage extends AppCompatActivity {
    private ImageView imageView;
    //Display full view of selected image
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_image);
        //Get selected image
        Intent intent = getIntent();
        int i = intent.getIntExtra(Config.KEY_BITMAP_ID, 0);
        //Check image resolution
        imageView = (ImageView) findViewById(R.id.imageViewFull);
        int width=GetAllImages.bitmaps[i].getWidth();
        int height=GetAllImages.bitmaps[i].getHeight();
        //Scale image when is too big
        if(width>4000 || height >4000){
            imageView.setImageBitmap(Bitmap.createScaledBitmap(GetAllImages.bitmaps[i],1200,1200,false));
        }else imageView.setImageBitmap(GetAllImages.bitmaps[i]);
    }
}
