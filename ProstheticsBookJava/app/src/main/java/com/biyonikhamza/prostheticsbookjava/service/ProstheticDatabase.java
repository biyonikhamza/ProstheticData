package com.biyonikhamza.prostheticsbookjava.service;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.biyonikhamza.prostheticsbookjava.model.Prosthetic;

@Database(entities = {Prosthetic.class} , version = 2)
public abstract class ProstheticDatabase extends RoomDatabase {

    public abstract ProstheticDao prostheticDao();

}
