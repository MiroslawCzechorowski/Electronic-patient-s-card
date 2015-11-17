package com.example.aa.electronicpatientscard;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MiroslawCzechorowski on 30.10.2015.
 */
//Create view of images downloaded from server
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private String[] urls;
    private Bitmap[] bitmaps;

    //Constructor
    public ImageAdapter(Context context, String[] urls, Bitmap[] bitmaps) {
        super();
        this.context = context;
        this.urls = urls;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public Object getItem(int position) {
        return urls[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Create view of images
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.list_images, parent, false);
        TextView textView =(TextView)convertView.findViewById(R.id.textViewURL);
        textView.setText(/*urls[position]*/"");
        //Display image in 200x200 resolution
        ImageView imageView =(ImageView)convertView.findViewById(R.id.imageDownloaded);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position], 200, 200, false));
        return convertView;
    }

}