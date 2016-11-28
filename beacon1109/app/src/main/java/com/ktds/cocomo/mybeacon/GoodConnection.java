package com.ktds.cocomo.mybeacon;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-08-17.
 */
public class GoodConnection {
    private String isgood, URL;
    private String major;
    private String m_name;

    private  int count=0;

    public GoodConnection(String major, String m_name, String isgood , String url) {
        this.major = major;
        this.m_name = m_name;
        this.isgood = isgood;
        this.URL = url;
    }

    public boolean requestLogin() {
                String[] getJsonData = {"","",""};
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                try{
                    String msg1 = major;
                    String msg2 = m_name;
                    String msg3 = isgood;
                    nameValuePairs.add(new BasicNameValuePair("major",msg1));
                    nameValuePairs.add(new BasicNameValuePair("isgood",msg3));
                    nameValuePairs.add(new BasicNameValuePair("m_name",msg2));
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

                }catch (Exception e){
                    return false;
                }
        return true;
    }
}
