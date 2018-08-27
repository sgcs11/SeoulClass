package com.sc.jn.seoulclass.Model;

import java.io.Serializable;

public class ClassListItem implements Serializable{
    private String id;
    private String title; // 제목
    private String location; //장소
    private String maxclassnm; //대분류명
    private String minclassnm; //소분류명
    private String pay; //결제방법
    private String usetgtinfo; //서비스대상
    private String url;  // 바로가기 url
    private String opnbgndt;//서비스개시시작일시
    private String opnenddt;//서비스개시종료일시
    private String rcptbgnt;//접수시작일시
    private String rcptenddt;//접수종료일시

    public ClassListItem() {
    }

    public ClassListItem(String id, String title, String location, String maxclassnm, String minclassnm, String pay, String usetgtinfo, String url, String opnbgndt, String opnenddt, String rcptbgnt, String rcptenddt) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.maxclassnm = maxclassnm;
        this.minclassnm = minclassnm;
        this.pay = pay;
        this.usetgtinfo = usetgtinfo;
        this.url = url;
        this.opnbgndt = opnbgndt;
        this.opnenddt = opnenddt;
        this.rcptbgnt = rcptbgnt;
        this.rcptenddt = rcptenddt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaxclassnm() {
        return maxclassnm;
    }

    public void setMaxclassnm(String maxclassnm) {
        this.maxclassnm = maxclassnm;
    }

    public String getMinclassnm() {
        return minclassnm;
    }

    public void setMinclassnm(String minclassnm) {
        this.minclassnm = minclassnm;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getUsetgtinfo() {
        return usetgtinfo;
    }

    public void setUsetgtinfo(String usetgtinfo) {
        this.usetgtinfo = usetgtinfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOpnbgndt() {
        return opnbgndt;
    }

    public void setOpnbgndt(String opnbgndt) {
        this.opnbgndt = opnbgndt;
    }

    public String getOpnenddt() {
        return opnenddt;
    }

    public void setOpnenddt(String opnenddt) {
        this.opnenddt = opnenddt;
    }

    public String getRcptbgnt() {
        return rcptbgnt;
    }

    public void setRcptbgnt(String rcptbgnt) {
        this.rcptbgnt = rcptbgnt;
    }

    public String getRcptenddt() {
        return rcptenddt;
    }

    public void setRcptenddt(String rcptenddt) {
        this.rcptenddt = rcptenddt;
    }
}
