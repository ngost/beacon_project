package com.ktds.cocomo.mybeacon;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class EventListAdapter extends BaseAdapter {

    ArrayList<String> e_subject = new ArrayList<String>();
    ArrayList<String> e_content = new ArrayList<String>();
    ArrayList<String> e_url = new ArrayList<String>();
    ArrayList<String> e_ing = new ArrayList<String>();
    ArrayList<Bitmap> bm = new ArrayList<Bitmap>();
    private E_ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;

    Context context = null;
    int[] imageIDs = null;
    int cposition;
    public EventListAdapter(Context context, ArrayList<EventItem> items)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        for(int i = 0;i<items.size();i++)
        {
            e_subject.add(items.get(i).e_subject);
            e_content.add(items.get(i).e_content);
            e_ing.add(items.get(i).e_ing);
            e_url.add(items.get(i).e_url);
            bm.add(setBmpFromUrl(e_url.get(i)));
        }
    }

    public int getCount() {
        return (null != e_subject) ? e_subject.size() : 0;
    }

    public Object getItem(int position) {
        return (null != e_subject) ? position : 0;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //cposition = position;
        View v = convertView;
        ArrayList<String> listenerPara = new ArrayList<String>();
        listenerPara.add(e_subject.get(position));
        listenerPara.add(e_content.get(position));
        listenerPara.add(e_url.get(position));

        if (v == null) {
            viewHolder = new E_ViewHolder();
            v = inflater.inflate(R.layout.event_item, null);
            viewHolder.subjectText = (TextView) v.findViewById(R.id.subjecttext);
            viewHolder.ingText = (TextView) v.findViewById(R.id.ingtext);
            viewHolder.image = (ImageView) v.findViewById(R.id.eventImg);
            v.setTag(viewHolder);
        }else
        {
            viewHolder = (E_ViewHolder)v.getTag();
        }
        viewHolder.subjectText.setText(e_subject.get(position));

        if(e_ing.get(position).equals("0"))
        {
            viewHolder.ingText.setText("진행중인 이벤트");
        }else
        {
            viewHolder.ingText.setText("종료된 이벤트");
        }


        if(bm.get(position)!=null) {
            viewHolder.image.setAdjustViewBounds(true);
            viewHolder.image.setImageBitmap(bm.get(position));
        }else
        {
            viewHolder.image.setAdjustViewBounds(true);
            viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        }

        EventClickListener eventClickListener = new EventClickListener(context, listenerPara);
        v.setOnClickListener(eventClickListener);

        return v;
    }
    public Bitmap setBmpFromUrl(String url) {
        Bitmap bmp=null;
        try{
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bmp = BitmapFactory.decodeStream(is);
            is.close();
            return bmp;
        }catch (Exception e){
            Log.d("실패","파일셋팅 오류");
            return null;
        }
    }
}

class E_ViewHolder{
    public TextView subjectText = null;
    public TextView ingText = null;
    public ImageView image = null;
}



