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
public class EventConnection {
    private String simpleData, URL;
    private ArrayList<String> major;
    private ArrayList<String> e_subject;
    private ArrayList<String> e_content;
    private ArrayList<String> e_url;
    private ArrayList<String> e_ing;
    private  int count=0;

    public EventConnection(String data, String url) { //major 값과 url값을 입력받아 서버의 menu와 관련된 데이터를 전부 꺼내오는 클래스다
        this.major = new ArrayList<String>();
        this.e_subject = new ArrayList<String>();
        this.e_content = new ArrayList<String>();
        this.e_url = new ArrayList<String>();
        this.e_ing = new ArrayList<String>();
        this.simpleData = data;
        this.URL = url;
    }

    public boolean requestLogin() {
                String[] getJsonData = {"","","","",""};
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
                        getJsonData[1] = json.getString("e_subject");
                        getJsonData[2] = json.getString("e_content");
                        getJsonData[3] = json.getString("e_url");
                        getJsonData[4] = json.getString("e_ing");
                        major.add(getJsonData[0]);
                        e_subject.add(getJsonData[1]);
                        e_content.add(getJsonData[2]);
                        e_url.add(getJsonData[3]);
                        e_ing.add(getJsonData[4]);
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
    public String getSubject(int index){return e_subject.get(index);}
    public String getContent(int index){return e_content.get(index);}
    public String getUrl(int index){return e_url.get(index);}
    public String getIng(int index){return e_ing.get(index);}
}
