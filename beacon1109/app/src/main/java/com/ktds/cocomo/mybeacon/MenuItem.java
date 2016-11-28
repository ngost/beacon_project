package com.ktds.cocomo.mybeacon;

/**
 * Created by jinyoung on 2016-11-02.
 */
class MenuItem{
    public String major;
    public String name;
    public String price;
    public String url;
    public String content;
    public String prefer;
    MenuItem(String major, String name, String price, String url, String content, String prefer)
    {
        this.major = major;
        this.name = name;
        this.price = price;
        this.url = url;
        this.content = content;
        this.prefer = prefer;
    }
}
