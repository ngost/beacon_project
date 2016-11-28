package com.ktds.cocomo.mybeacon;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-08-17.
 */
public class MenuConnection {
    private String simpleData, URL;
    private ArrayList<String> major;
    private ArrayList<String> m_name;
    private ArrayList<String> m_price;
    private ArrayList<String> m_url;
    private ArrayList<String> m_content;
    private ArrayList<String> m_preference;
    private  int count=0;

    public MenuConnection(String data, String url) { //major 값과 url값을 입력받아 서버의 menu와 관련된 데이터를 전부 꺼내오는 클래스다
        this.major = new ArrayList<String>();
        this.m_name = new ArrayList<String>();
        this.m_price = new ArrayList<String>();
        this.m_url = new ArrayList<String>();
        this.m_content = new ArrayList<String>();
        this.m_preference = new ArrayList<String>();

        this.simpleData = data;
        this.URL = url;
        Log.d("111","111");
    }

    public boolean requestLogin() {
                String[] getJsonData = {"","","","","",""};
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                try{
                    String msg = simpleData;
                    nameValuePairs.add(new BasicNameValuePair("major",msg));

                    HttpPost post = new HttpPost(URL);
                    Log.d("222","222");
                    HttpClient client2 = new DefaultHttpClient();
                    UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                    post.setEntity(entityRequest);
                    HttpResponse response = client2.execute(post); // 보낸 뒤, 리턴 되는 결과값을 받음

                    BufferedReader bufreader = new BufferedReader(
                            new InputStreamReader(
                                    response.getEntity().getContent(), "euc-kr"));

                    String page = "";
                    String line = null;
                    while ((line = bufreader.readLine()) != null) {
                        page += line;

                    }
                    Log.d("page", page);
                    // 읽어들인 내용을 json 객체에 담아 그 중 dataSend로 정의 된 내용을
                    // 불어온다. 그럼 json 중 원하는 내용을 하나의 json 배열에 담게 된다.

                    JSONObject json = new JSONObject(page);
                    JSONArray jArr = json.getJSONArray("dataSend");

                    // JSON이 가진 크기만큼 데이터를 받아옴

                    Log.d("33312",jArr.length()+"");
                    for (int i =0; i<jArr.length(); i++){

                        json = jArr.getJSONObject(i);
                        getJsonData[0] = json.getString("major");
                        getJsonData[1] = json.getString("m_name");
                        getJsonData[2] = json.getString("m_price");
                        getJsonData[3] = json.getString("m_url");
                        getJsonData[4] = json.getString("m_content");
                        getJsonData[5] = json.getString("m_preference");

                        major.add(getJsonData[0]);
                        m_name.add(getJsonData[1]);
                        m_price.add(getJsonData[2]);
                        m_url.add(getJsonData[3]);
                        m_content.add(getJsonData[4]);
                        m_preference.add(getJsonData[5]);


                    }
                    count = jArr.length();
                    Log.d("count", count+"");
                }catch (Exception e){
                }




        return true;

    }

    public int getCount(){
        return count;
    }
    public String getMajor(int index){return major.get(index);}
    public String getName(int index){
        return m_name.get(index);
    }
    public String getPrice(int index){return m_price.get(index);}
    public String getUrl(int index){return m_url.get(index);}
    public String getContent(int index){return m_content.get(index);}
    public String getPrefer(int index){return m_preference.get(index);}


}
