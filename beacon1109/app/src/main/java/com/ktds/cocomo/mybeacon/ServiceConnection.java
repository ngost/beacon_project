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
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-08-17.
 */
public class ServiceConnection {
    private String simpleData, URL;
    private ArrayList<String> major;
    private ArrayList<String> s_name;
    private ArrayList<String> s_info;
    private ArrayList<String> s_url;
    private  int count=0;

    public ServiceConnection(String data, String url) { //major 값과 url값을 입력받아 서버의 menu와 관련된 데이터를 전부 꺼내오는 클래스다
        this.major = new ArrayList<String>();
        this.s_name = new ArrayList<String>();
        this.s_info = new ArrayList<String>();
        this.s_url = new ArrayList<String>();
        this.simpleData = data;
        this.URL = url;
        Log.d("111","111");
    }

    public boolean requestLogin() {
                String[] getJsonData = {"","","",""};
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
                        getJsonData[1] = json.getString("s_name");
                        getJsonData[2] = json.getString("s_info");
                        getJsonData[3] = json.getString("s_url");

                        major.add(getJsonData[0]);
                        s_name.add(getJsonData[1]);
                        s_info.add(getJsonData[2]);
                        s_url.add(getJsonData[3]);
                    }
                    count = jArr.length();
                    Log.d("count", count+"");
                }catch (Exception e){
                    return false;
                }
        return true;

    }
    public int getCount(){
        return count;
    }
    public String getMajor(int index){return major.get(index);}
    public String getName(int index){
        return s_name.get(index);
    }
    public String getInfo(int index){return s_info.get(index);}
    public String getUrl(int index){return s_url.get(index);}
}
