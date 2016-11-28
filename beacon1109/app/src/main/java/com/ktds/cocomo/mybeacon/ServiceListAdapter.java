package com.ktds.cocomo.mybeacon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ServiceListAdapter extends BaseAdapter {

    ArrayList<String> s_name = new ArrayList<String>();
    ArrayList<String> s_info = new ArrayList<String>();
    ArrayList<String> s_url = new ArrayList<String>();
    ArrayList<Bitmap> bm = new ArrayList<Bitmap>();
    private S_ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    Bitmap setbm;

    //Bitmap bm;

    Context context = null;
    int[] imageIDs = null;
    int cposition;
    public ServiceListAdapter(Context context, ArrayList<ServiceItem> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        for(int i = 0;i<items.size();i++)
        {
            s_name.add(items.get(i).s_name);
            s_info.add(items.get(i).s_info);
            s_url.add(items.get(i).s_url);
            bm.add(setBmpFromUrl(i));
        }
    }

    public int getCount() {
        return (null != s_name) ? s_name.size() : 0;
    }

    public Object getItem(int position) {
        return (null != s_name) ? position : 0;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        cposition = position;
        View v = convertView;
        ArrayList<String> listenerPara = new ArrayList<String>();
        listenerPara.add(s_name.get(position));
        listenerPara.add(s_info.get(position));
        listenerPara.add(s_url.get(position));

        if (v == null) {
            viewHolder = new S_ViewHolder();
            v = inflater.inflate(R.layout.service_item, null);
            viewHolder.s_name= (TextView) v.findViewById(R.id.s_nameText);
        //    viewHolder.s_info = (TextView) v.findViewById(R.id.s_infoText);
            viewHolder.image = (ImageView) v.findViewById(R.id.serviceImg);
            v.setTag(viewHolder);
        }else
        {
            viewHolder = (S_ViewHolder)v.getTag();
        }
        viewHolder.s_name.setText(s_name.get(cposition));
        //viewHolder.s_info.setText(s_info.get(cposition));

        if(bm.get(cposition)!=null) {
            viewHolder.image.setAdjustViewBounds(true);
            viewHolder.image.setImageBitmap(bm.get(cposition));
        }else
        {
            viewHolder.image.setAdjustViewBounds(true);
            viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        }

        ServiceClickListener serviceClickListener = new ServiceClickListener(context, listenerPara);
        v.setOnClickListener(serviceClickListener);

        return v;
    }

        /*
        final ImageView i = new ImageView(this.context);
        Log.d("dd",String.valueOf(position));
        */

        //TextView tvName = new TextView();



        /*
        Thread tThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL aURL = new URL(m_name.get(cposition));
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);

                    //가져오는 이미지의 해상도 알아내기


                    //bm = decodeSampledBitmapFromIs(bis, 300, 240);

                   // i.setImageBitmap(decodeSampledBitmapFromIs(bis, 100, 100));

                    //i.setImageBitmap(Bitmap.createScaledBitmap(bm, 320, 240, false));
                    //bm = BitmapFactory.decodeStream(bis);
                    bm = Bitmap.createScaledBitmap(decodeSampledBitmapFromIs(bis, 30, 40), 150, 200, false);
                    i.setAdjustViewBounds(true);
                    i.setImageBitmap(bm);
                    bis.close();
                    is.close();

                } catch (IOException e) {
                    Log.e("DEBUGTAG", "Remtoe Image Exception", e);
                }
            }
        });
        tThread.start();
        try{
            tThread.join();
        }catch (InterruptedException e)
        {
        }
*/


        //ImageClickListener imageViewClickListener = new ImageClickListener(context,imageUrl.get(position) );
        // i.setOnClickListener(imageViewClickListener);

        //return i;

        //i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //i.setLayoutParams(new Gallery.LayoutParams(300, 300));


/*
        if (null != convertView)
            imageView = (ImageView)convertView;
        else {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), imageIDs[position]);
            bmp = Bitmap.createScaledBitmap(bmp, 320, 240, false);

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(bmp);


        }
*/

    public Bitmap decodeSampledBitmapFromIs(BufferedInputStream bis,
                                            int reqWidth, int reqHeight) {

    // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeStream(bis, null, options);

    // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        Log.i("decodeFromIs:", "width" + reqWidth + "height" + reqHeight + "inSampleSize :" + options.inSampleSize);

    // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //try{;}catch (IOException e){}

        Bitmap b = BitmapFactory.decodeStream(bis, null, options);
        return b;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 8;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public Bitmap setBmpFromUrl(int position) {

        cposition = position;
        Thread tThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL aURL;
                    aURL = new URL(s_url.get(cposition));
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    //BufferedInputStream bis = new BufferedInputStream(is);

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    setbm = BitmapFactory.decodeStream(is);
                    //bm = Bitmap.createScaledBitmap(decodeSampledBitmapFromIs(bis, 30, 40), 150, 200, false);


                    //image.setAdjustViewBounds(true);
                    //image.setImageBitmap(bm);
                    //bis.close();
                    is.close();

                } catch (IOException e) {

                    Log.e("DEBUGTAG", "이 이미지는 못가져왔어", e);

                }
            }
        });

        tThread.start();
        try {
            tThread.join();
            //viewHolder.image.setAdjustViewBounds(true);
            //viewHolder.image.setImageBitmap(bm);

        } catch (Exception e) {
            Log.d("error", "error");
        }
        return setbm;
    }
}

class S_ViewHolder{
    public TextView s_name = null;
    public TextView s_info = null;
    public ImageView image = null;
}

