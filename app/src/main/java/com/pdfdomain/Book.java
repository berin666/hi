package com.pdfdomain;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Book extends SugarRecord implements Serializable{

    private String titel;
    private String type;
    private String score;
    private String source;
    private long category;
    private String desc;
    private String pgCnt;
    private String flNm;
    private String MD5;
    private String foundUrl;
    private String language;
    private String imFlPth;
    private String ixDate;
    private String id;

    public boolean isFavorite(){
        return !Book.find(Book.class, "MD5 = ?", getMD5()).isEmpty();
    }

    public String getTitel() {
        return titel;
    }

    public String getType() {
        return type;
    }

    public String getScore() {
        return score;
    }

    public String getSource() {
        return source;
    }

    public long getCategory() {
        return category;
    }

    public String getDesc() {
        return desc;
    }

    public String getPgCnt(){ return pgCnt;
    }

    public String getFlNm() { return flNm; }

    public String getMD5() { return MD5; }

    public String getFoundUrl() {
        return foundUrl;
    }

    public String getLanguage() { return language; }

    public String getImFlPth() { return imFlPth; }

    public String getID(){return id; }
    public String getIxDate(){return ixDate; }

}