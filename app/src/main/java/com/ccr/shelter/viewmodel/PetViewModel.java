package com.ccr.shelter.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ccr.shelter.petData.Pet;
import com.ccr.shelter.petData.PetDB;
import com.ccr.shelter.petData.PetRepo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PetViewModel extends AndroidViewModel {

    private PetRepo mRepository;
    private LiveData<List<Pet>> mAllPets;

    public PetViewModel(Application application){
        super(application);
        mRepository = new PetRepo(PetDB.getDatabase(application).petDAO());
        mAllPets = mRepository.getAllPets();
    }

    public LiveData<List<Pet>> getAllPets() {
        return mAllPets;
    }

    public Pet getPet(int id) {
        Pet pet = null;
        try {
            pet = mRepository.getPet(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pet;
    }

    public void insert(Pet pet) {mRepository.insert(pet);}

    public void delete(Pet pet){mRepository.delete(pet);}

    public void update(Pet pet){mRepository.update(pet);}


}
