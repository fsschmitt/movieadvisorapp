package com.movieadvisor;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    private ArrayList<Bitmap> bImages;

    public GalleryAdapter(Context c, ArrayList<Bitmap> bImages) {
    	this.bImages = bImages;
    	mContext = c;
    	TypedArray attr = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
        mGalleryItemBackground = attr.getResourceId(
                R.styleable.HelloGallery_android_galleryItemBackground, 0);
        attr.recycle();
    }

    public int getCount() {
        return bImages.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(bImages.get(position));
        imageView.setLayoutParams(new Gallery.LayoutParams(150, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return imageView;
    }
}