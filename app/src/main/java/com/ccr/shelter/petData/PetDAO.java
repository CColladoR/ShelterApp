package com.ccr.shelter.petData;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao

public interface PetDAO {

    @Insert
    void insert(Pet pet);

    @Query("SELECT * from pet_table")
    LiveData<List<Pet>> getPets();

    @Update
    void update(Pet pet);

    @Query("SELECT * FROM pet_table WHERE id == :id")
    Pet getPet(int id);

    @Delete
    void delete(Pet pet);
}
