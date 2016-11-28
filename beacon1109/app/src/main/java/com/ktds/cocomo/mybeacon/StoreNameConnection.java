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
public class StoreNameConnection {
    private String simpleData, URL;
    private String name;
    public StoreNameConnection(String data, String url) { //major 값과 url값을 입력받아 서버의 menu와 관련된 데이터를 전부 꺼내오는 클래스다
        name = "알수없음";
        this.simpleData = data;
        this.URL = url;
    }

    public String requestData() {
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
                    Log.d("Store_page=",page);
                    name = page;
                }catch (Exception e){
                }
        return name;
    }
}
