package com.ccr.shelter.petData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "pet_table")
public class Pet {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String breed;
    private int gender;
    private int weight;
    private String birthdate;
    private int sterilized;
    private int vaccinated;
    private int dewormed;
    private String details;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;


    public Pet(int id,@NonNull String name,@NonNull String breed, int gender, String birthdate, int sterilized, int vaccinated, int dewormed, int weight, String details, byte[] image) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
        this.image = image;
        this.birthdate = birthdate;
        this.sterilized = sterilized;
        this.vaccinated = vaccinated;
        this.dewormed = dewormed;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getBreed() {
        return breed;
    }

    public int getGender() {
        return gender;
    }

    public int getWeight() {
        return weight;
    }

    public byte[] getImage() {
        return image;
    }

   public String getBirthdate(){
        return birthdate;
    }

    public int getSterilized(){
        return sterilized;
    }

    public int getVaccinated(){
        return vaccinated;
    }

    public int getDewormed(){
        return dewormed;
    }

    public String getDetails(){
        return details;
    }



    public static final int UNKNOWN = 0;
    public static final int MALE = 1;
    public static final int FEMALE = 2;

    public static final int NO = 0;
    public static final int YES = 1;

}
