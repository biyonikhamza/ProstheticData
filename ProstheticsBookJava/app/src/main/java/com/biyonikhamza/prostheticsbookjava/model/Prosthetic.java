package com.biyonikhamza.prostheticsbookjava.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Prosthetic implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "pName")
    public String pName;

    @ColumnInfo(name = "tradeMark")
    public String tradeMark;

    @ColumnInfo(name = "about")
    public String about;

    @ColumnInfo(name = "image")
    public byte[] image;

    public Prosthetic(String pName , String tradeMark , String about, byte[] image){
        this.pName = pName;
        this.tradeMark = tradeMark;
        this.about = about;
        this.image = image;
    }


}
