package com.example.aa.electronicpatientscard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ViewImages extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private String id;
    public GetAllImages getAlImages;
    private GridView gridView;
    private Button buttonUploadImage;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Images"));
        tabLayout.addTab(tabLayout.newTab().setText("Person"));
        tabLayout.addTab(tabLayout.newTab().setText("Observation"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==2){
                    Intent intent = new Intent(getApplicationContext(), ViewObservation.class);
                    intent.putExtra(Config.PATIENT_ID,id);
                    startActivity(intent);
                }else if (tab.getPosition()==1){
                    Intent intent = new Intent(getApplicationContext(), ViewPatient.class);
                    intent.putExtra(Config.PATIENT_ID,id);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent intent =getIntent();
        //Assign ID selected patient
        id=intent.getStringExtra(Config.PATIENT_ID);
        //UI
        gridView=(GridView)findViewById(R.id.gridView);
        buttonUploadImage=(Button)findViewById(R.id.buttonUploadImage);

        gridView.setOnItemClickListener(this);
        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        //Get images urls from server
        getURLs();
    }
    //Show image selector when uploading new images
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), PICK_IMAGE_REQUEST);
    }

    //Get selected image for upload
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Decode image to base64
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    //Upload image to server
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewImages.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getURLs();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                int width=bitmap.getWidth();
                int height=bitmap.getHeight();
                //Scale image when is too big
                if(width>3000 || height >3000) {
                    bitmap=bitmap.createScaledBitmap(bitmap,1600,1600, false);
                }
                String uploadImage = getStringImage(bitmap);
                HashMap<String,String> data = new HashMap<>();
                data.put(Config.KEY_UPLOAD, uploadImage);
                String result = rh.sendPostRequest(Config.URL_UPLOAD_IMAGES+id,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
    //Method that list all images in gridView
    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewImages.this,"Downloading images...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();
                Toast.makeText(ViewImages.this, "Success!", Toast.LENGTH_LONG).show();
                ImageAdapter imageAdapter= new ImageAdapter(ViewImages.this,GetAllImages.imageURLs,GetAllImages.bitmaps);
                gridView.setAdapter(imageAdapter);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getAlImages.getAllImages();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }
    //Download images urls from server
    private void getURLs() {
        class GetURLs extends AsyncTask<String,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewImages.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Show all images from received urls
                getAlImages = new GetAllImages(s);
                getImages();
            }
            //get images urls from selected patient
            @Override
            protected String doInBackground(String... strings) {
               RequestHandler rh = new RequestHandler();
                String s=rh.sendGetImgRequestParam(strings);
                return s;
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(Config.URL_GET_IMAGE+id);
    }
    //Handle full image view when clicked image
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ViewFullImage.class);
        intent.putExtra(Config.KEY_BITMAP_ID,i);
        startActivity(intent);
    }
}
