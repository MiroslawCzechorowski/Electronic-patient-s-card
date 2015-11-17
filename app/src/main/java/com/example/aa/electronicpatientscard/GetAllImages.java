package com.example.aa.electronicpatientscard;

/**
 * Created by MiroslawCzechorowski on 30.10.2015.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GetAllImages {
    public static String[] imageURLs;
    public static Bitmap[] bitmaps;

    private String json;
    private JSONArray urls;
    //Constructor
    public GetAllImages(String json){
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(json);
            urls = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //Get bitmap from url
    private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString(Config.TAG_IMAGE_URL));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }
    //Get all images from received urls and add them to bitmaps array
    public void getAllImages() throws JSONException {
        bitmaps = new Bitmap[urls.length()];

        imageURLs = new String[urls.length()];

        for(int i=0;i<urls.length();i++){
            imageURLs[i] = urls.getJSONObject(i).getString(Config.TAG_IMAGE_URL);
            JSONObject jsonObject = urls.getJSONObject(i);
            bitmaps[i]=getImage(jsonObject);
        }
    }
}
