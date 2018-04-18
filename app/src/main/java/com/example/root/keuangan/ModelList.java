package com.example.root.keuangan;

public class ModelList {
    private Integer id, no;
    private String title, nominal, date, isType;

    public ModelList(Integer no, Integer id, String title, String nominal, String date, String isType) {
        this.no      = no;
        this.id      = id;
        this.title   = title;
        this.nominal = nominal;
        this.date    = date;
        this.isType  = isType;
    }

    public int getNo() {
        return no;
    }
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getNominal() {
        return nominal;
    }
    public String getDate() {
        return date;
    }
    public String getIsType() {
        return isType;
    }

    public void setNoList(int no) {
        this.no = no;
    }
    public void setIdList(int id) {
        this.id = id;
    }
    public void setTitleList(String title) {
        this.title = title;
    }
    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
    public void setDate(String date) {
        this.date = date;
    }

}