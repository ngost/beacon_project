package com.ktds.cocomo.mybeacon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MenuListAdapter extends BaseAdapter{
    ArrayList<String> m_name = new ArrayList<String>();
    ArrayList<String> m_content = new ArrayList<String>();
    ArrayList<String> m_price = new ArrayList<String>();
    ArrayList<String> m_url = new ArrayList<String>();
    ArrayList<String> m_prefer = new ArrayList<String>();
    ArrayList<Bitmap> bm = new ArrayList<Bitmap>();
    ArrayList<Float> ratingResult = new ArrayList<Float>();
    ArrayList<Integer> isChange = new ArrayList<Integer>();
    ArrayList<Boolean> fullOrLeak = new ArrayList<Boolean>();
    M_ViewHolder viewHolder;
    //private M_ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    RatingBar ratingBar;
    Preference_Avg_Connection preference_avg_connection;

    Bitmap setbm;
    private String major = "";
    //Bitmap bm;

    Context context = null;
    int[] imageIDs = null;
    int cposition;
    private View.OnClickListener mOnClickListener = null;

    public MenuListAdapter(Context context, ArrayList<MenuItem> items, View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        major = items.get(0).major;
        for(int i = 0;i<items.size();i++)
        {
            m_name.add(items.get(i).name);
            m_content.add(items.get(i).content);
            m_price.add(items.get(i).price);
            m_url.add(items.get(i).url);
            m_prefer.add(items.get(i).prefer);
            bm.add(setBmpFromUrl(i));
            fullOrLeak.add(false);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                preference_avg_connection = new Preference_Avg_Connection(major,"http://211.227.149.160:8080/BeaconServer/beacon/Select_Preference.jsp" );
                preference_avg_connection.requestLogin();
            }
        });
        thread.start();
        try {
            thread.join();
            ratingResult = preference_avg_connection.prefer_avg();
        }catch (Exception e){
        }
    }

    public int getCount() {
        return (null != m_name) ? m_name.size() : 0;
    }

    public Object getItem(int position) {
        return (null != m_name) ? position : 0;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        cposition = position;
        View v = convertView;
        ArrayList<String> listenerPara = new ArrayList<String>();
        viewHolder = new M_ViewHolder();
        listenerPara.add(m_name.get(position));
        listenerPara.add(m_price.get(position));
        listenerPara.add(m_content.get(position));
        listenerPara.add(major);

        if (v == null) {
           //viewHolder = new M_ViewHolder();
            v = inflater.inflate(R.layout.menu_item, parent, false);
            viewHolder.nameText = (TextView) v.findViewById(R.id.nametext);
            viewHolder.priceText = (TextView) v.findViewById(R.id.pricetext);
            viewHolder.image = (ImageView) v.findViewById(R.id.menuImg);
            viewHolder.ratingBar = (RatingBar)v.findViewById(R.id.ratingbar2);
            viewHolder.RatingResult = (TextView)v.findViewById(R.id.ratingScore);
            viewHolder.goodText = (TextView)v.findViewById(R.id.goodText);
            viewHolder.goodButton = (ImageButton)v.findViewById(R.id.goodButton);


            v.setTag(viewHolder);
        }else
        {
            viewHolder = (M_ViewHolder)v.getTag();
        }

        viewHolder.ratingBar.setRating(ratingResult.get(position));
        viewHolder.nameText.setText(m_name.get(position));
        viewHolder.priceText.setText(m_price.get(position)+"원");
        viewHolder.goodText.setText("좋아요 "+m_prefer.get(position));


        if(String.valueOf(ratingResult.get(position)).length()>3 )
        {
            viewHolder.RatingResult.setText(" "+(String.valueOf(ratingResult.get(position))).substring(0, 3));
        }else
        {
            viewHolder.RatingResult.setText(" "+ratingResult.get(position)+"");
        }


        if(bm.get(cposition)!=null) {
            viewHolder.image.setAdjustViewBounds(true);
            viewHolder.image.setImageBitmap(bm.get(position));
        }else
        {
            viewHolder.image.setAdjustViewBounds(true);
            viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        }

        MenuClickListener menuClickListener = new MenuClickListener(context, listenerPara, bm.get(position));
        v.setOnClickListener(menuClickListener);


        //좋아요 리스너
        if(isChange.contains(position))
        {
            if(fullOrLeak.get(position)==true)
            {
                viewHolder.goodButton.setImageResource(R.drawable.love);
            }
            else if(fullOrLeak.get(position)==false)
            {
                viewHolder.goodButton.setImageResource(R.drawable.love2);
            }
        }else
        {
            viewHolder.goodButton.setImageResource(R.drawable.love2);
        }
        viewHolder.goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange.add(position);
                notifyDataSetChanged();

                if(fullOrLeak.get(position)==false)
                {
                    int prefer = 1+Integer.parseInt(m_prefer.get(position));
                    m_prefer.set(position,String.valueOf(prefer));
                    String output = m_prefer.get(position);
                    viewHolder.goodText.setText("좋아요 "+output);
                    ((ImageButton) v).setImageResource(R.drawable.love);
                    fullOrLeak.set(position,true);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GoodConnection goodConnection = new GoodConnection(major,m_name.get(position),String.valueOf(1)
                                    ,"http://211.227.149.160:8080/BeaconServer/beacon/Update_Good.jsp");
                            goodConnection.requestLogin();
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    }catch (Exception e){
                    }


                }else if(fullOrLeak.get(position)==true)
                {
                    int prefer = Integer.parseInt(m_prefer.get(position))-1;
                    m_prefer.set(position,String.valueOf(prefer));
                    String output = m_prefer.get(position);
                    viewHolder.goodText.setText("좋아요 "+output);
                    ((ImageButton) v).setImageResource(R.drawable.love2);
                    fullOrLeak.set(position,false);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GoodConnection goodConnection = new GoodConnection(major,m_name.get(position),String.valueOf(-1)
                                    ,"http://211.227.149.160:8080/BeaconServer/beacon/Update_Good.jsp");
                            goodConnection.requestLogin();
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    }catch (Exception e){
                    }
                }

            }
        });

        return v;
    }



    public Bitmap setBmpFromUrl(int position) {

        cposition = position;
        Thread tThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL aURL;
                    aURL = new URL(m_url.get(cposition));
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    setbm = BitmapFactory.decodeStream(is);
                    is.close();

                } catch (IOException e) {

                    Log.e("DEBUGTAG", "이 이미지는 못가져왔어", e);

                }
            }
        });

        tThread.start();
        try {
            tThread.join();
        } catch (Exception e) {
            Log.d("error", "error");
        }
        return setbm;
    }
}
class M_ViewHolder{
    public TextView nameText = null;
    public TextView priceText = null;
    public ImageView image = null;
    public RatingBar ratingBar = null;
    public TextView RatingResult = null;
    public ImageButton goodButton = null;
    public TextView goodText = null;
}

