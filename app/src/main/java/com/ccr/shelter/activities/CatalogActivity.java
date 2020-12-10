package com.ccr.shelter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.ccr.shelter.R;
import com.ccr.shelter.adapter.PetListAdapter;
import com.ccr.shelter.petData.Pet;
import com.ccr.shelter.viewmodel.PetViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CatalogActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private PetViewModel mPetViewModel;
    SearchView mSearchView;
    View mShelterEmpty;
    private PetListAdapter mAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Shelter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        setUI();

        mAdapter = new PetListAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPetViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        mPetViewModel.getAllPets().observe(this, pets -> {

            mAdapter.setPets(pets);

            if(mAdapter.getItemCount() != 0) {
                mShelterEmpty.setVisibility(View.GONE);
            }
            else {
                mShelterEmpty.setVisibility(View.VISIBLE);
            }
        });
    setSearchView();
    }

    void setUI() {

        mShelterEmpty = findViewById(R.id.empty_shelter);
        mSearchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerview);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });

    }


    void setSearchView() {
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);


        assert searchManager != null;
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (containsName(PetListAdapter.allPets, s.toUpperCase())) {
                    mAdapter.getFilter().filter(s);
                } else {
                    Toast.makeText(getBaseContext(),
                            "No encontrado",
                            Toast.LENGTH_LONG)
                            .show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    boolean containsName(List<Pet> list, String name){
        for (Pet pet :
                list) {
            if(pet.getName().toUpperCase().contains(name))
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSearchView.setQuery("", false);
        mSearchView.clearFocus();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Pet pet = new Pet(
                    0,
                    Objects.requireNonNull(data.getStringExtra("Name")),
                    Objects.requireNonNull(data.getStringExtra("Breed")),
                    data.getIntExtra("Gender", 0),
                    data.getIntExtra("Weight", 0),
                    data.getByteArrayExtra("Image")
                   // data.getStringExtra("Date"),
                    //data.getStringExtra("Details")
                    );


            mPetViewModel.insert(pet);

        } else if(resultCode != RESULT_OK) {

            Toast.makeText(
                    getApplicationContext(),
                    "No se ha guardado",
                    Toast.LENGTH_LONG).show();
        }

    }

}