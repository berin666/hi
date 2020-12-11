package com.pdfdomain;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Book extends SugarRecord implements Serializable{

    private String Title;
    private String Author;
    private String Year;
    private String Publisher;
    private long Filesize;
    private String Extension;
    private String Pages;
    private String Coverurl;
    private String MD5;
    private String Edition;
    private String Language;
    private String Scanned;
    private String fileName;



    public boolean isFavorite(){
        return !Book.find(Book.class, "MD5 = ?", getMD5()).isEmpty();
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getYear() {
        return Year;
    }

    public String getPublisher() {
        return Publisher;
    }

    public long getFilesize() {
        return Filesize;
    }

    public String getExtension() {
        return Extension;
    }

    public String getPages() {
        return Pages;
    }

    public String getCoverurl() { return Coverurl; }

    public String getMD5() { return MD5; }

    public String getEdition() {
        return Edition;
    }

    public String getLanguage() { return Language; }

    public String getScanned() { return Scanned; }



}