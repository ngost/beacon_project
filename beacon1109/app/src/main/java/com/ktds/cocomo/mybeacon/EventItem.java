package com.ktds.cocomo.mybeacon;

/**
 * Created by jinyoung on 2016-11-02.
 */
class EventItem {
    public String e_subject;
    public String e_content;
    public String e_url;
    public String e_ing;

    EventItem(String subject, String content, String url, String ing)
    {
        this.e_subject = subject;
        this.e_content = content;
        this.e_url = url;
        this.e_ing = ing;
    }

}
