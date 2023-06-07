package com.biyonikhamza.prostheticsbookjava.service;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.biyonikhamza.prostheticsbookjava.model.Prosthetic;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ProstheticDao {

    @Query("SELECT * FROM Prosthetic")
    Flowable<List<Prosthetic>> getAll();

    @Query("SELECT * FROM Prosthetic WHERE id = :id")
    Flowable<Prosthetic> id(int id);

    @Insert
    Completable insertAll(Prosthetic prosthetic);

    @Delete
    Completable delete(Prosthetic prosthetic);

}
