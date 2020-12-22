package com.ccr.shelter.petData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    private String details;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;
    private int ster;
    private int vacc;

    private int adopted;
    private String adoptDate;


    public Pet(int id,@NonNull String name,@NonNull String breed, int gender, String birthdate, int weight, String details, byte[] image, int ster, int vacc, int adopted, String adoptDate) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.birthdate = birthdate;
        this.weight = weight;
        this.details = details;
        this.image = image;
        this.ster = ster;
        this.vacc = vacc;
        this.adopted = adopted;
        this.adoptDate = adoptDate;

    }

    //Getters

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

    public String getDetails(){
        return details;
    }

    public int getSter(){
        return ster;
    }

    public int getVacc(){
        return vacc;
    }

    public int getAdopted(){
        return adopted;
    }

    public String getAdoptDate(){
        return adoptDate;
    }

    //Values

    public static final int UNKNOWN = 0;
    public static final int MALE = 1;
    public static final int FEMALE = 2;

    public static final int NO = 0;
    public static final int YES = 1;

}
