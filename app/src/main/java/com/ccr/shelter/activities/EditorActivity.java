package com.ccr.shelter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ccr.shelter.R;
import com.ccr.shelter.petData.Pet;
import com.ccr.shelter.viewmodel.PetViewModel;

import java.io.ByteArrayOutputStream;

public class EditorActivity extends AppCompatActivity {

    private PetViewModel mPetViewModel;

    private static int RESULT_LOAD_IMAGE = 1;

    private EditText mNameEditText;

    private EditText mBreedEditText;

    private EditText mWeightEditText;

    private Spinner mGenderSpinner;

    private ImageView mImageView;

    Bundle extras;

    int id;
    String name;
    private int mGender = 0;
    String breed;
    int weight;
    byte[] imageAsByteArray;
    String details;
    String birthdate;


    final int PIC_CROP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        setViews();

        setupSpinner();

        extras = getIntent().getExtras();

        if (extras != null) {
            this.setTitle(R.string.update_pet);
            id = extras.getInt("Id");

            mPetViewModel = new ViewModelProvider(this).get(PetViewModel.class);

            Pet selectedPet = mPetViewModel.getPet(id);

            mNameEditText.setText(selectedPet.getName());
            mBreedEditText.setText(selectedPet.getBreed());
            mWeightEditText.setText(String.valueOf(selectedPet.getWeight()));
            setGender(selectedPet);
            Bitmap bmp = BitmapFactory.decodeByteArray(selectedPet.getImage(), 0, selectedPet.getImage().length);
            mImageView.setImageBitmap(bmp);
        }
    }

    void setViews() {
        mNameEditText = findViewById(R.id.edit_pet_name);
        mBreedEditText = findViewById(R.id.edit_pet_breed);
        mWeightEditText = findViewById(R.id.edit_pet_weight);
        mGenderSpinner = findViewById(R.id.spinner_gender);
        mImageView = findViewById(R.id.pet_image);
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create mAdapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = Pet.MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = Pet.FEMALE;
                    } else {
                        mGender = Pet.UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }


    private void insertPet() {

        getValues();

        Intent replyIntent = new Intent();

        if ((TextUtils.isEmpty(mNameEditText.getText())) || (TextUtils.isEmpty(mBreedEditText.getText()))) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {

            replyIntent.putExtra("Name", name);
            replyIntent.putExtra("Breed", breed);
            replyIntent.putExtra("Gender", mGender);
            replyIntent.putExtra("Weight", weight);
            replyIntent.putExtra("Image", imageAsByteArray);
            replyIntent.putExtra("Date", birthdate);
            replyIntent.putExtra("Details", details);

            setResult(RESULT_OK, replyIntent);
        }

        finish();
    }

    private void updatePet() {
        getValues();
        mPetViewModel.update(new Pet(id, name, breed, mGender, weight, imageAsByteArray, birthdate, details));
        finish();
    }

    private void deletePet() {
        getValues();
        mPetViewModel.delete(new Pet(id, name, breed, mGender, weight, imageAsByteArray, birthdate, details));
        finish();
    }

    void setGender(Pet pet) {
        mGenderSpinner.setSelection(pet.getGender());
    }

    void getValues() {
        name = mNameEditText.getText().toString();
        breed = mBreedEditText.getText().toString();
        weight = Integer.parseInt(mWeightEditText.getText().toString());
        BitmapDrawable imageDrawable = (BitmapDrawable) mImageView.getDrawable();
        imageAsByteArray = getBytesFromBitmap(imageDrawable.getBitmap());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);

        if (extras != null) {
            menu.findItem(R.id.action_delete).setVisible(true);
        } else {
            menu.findItem(R.id.action_delete).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_save:
                if (extras != null)
                    updatePet();
                else
                    insertPet();
                return true;
            case R.id.action_delete:
                deletePet();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public void onClickPickImage(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            performCrop(imageUri);
        }

        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = null;
                if (extras != null) {
                    selectedBitmap = extras.getParcelable("data");
                }

                mImageView.setImageBitmap(selectedBitmap);

            }
        }
    }
}