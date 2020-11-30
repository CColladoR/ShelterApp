package com.ccr.shelter.petData;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.Future;

public class PetRepo {

    private PetDAO mPetDao;
    private LiveData<List<Pet>> mAllPets;

    public PetRepo(PetDAO petDao) {
        mPetDao = petDao;
        mAllPets = mPetDao.getPets();
    }

    public LiveData<List<Pet>> getAllPets() {
        return mAllPets;
    }

    public void insert(final Pet pet) {
        PetDB.databaseWriteExecutor.execute(() -> mPetDao.insert(pet));
    }

    public void delete(final Pet pet) {
        PetDB.databaseWriteExecutor.execute(() -> mPetDao.delete(pet));
    }

    public void update(final Pet pet) {
        PetDB.databaseWriteExecutor.execute(() -> mPetDao.update(pet));
    }

    public Future<Pet> getPet(final int id) {
        return PetDB.databaseWriteExecutor.submit(() -> mPetDao.getPet(id));
    }
}
