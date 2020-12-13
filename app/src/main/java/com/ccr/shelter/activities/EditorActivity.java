package com.ccr.shelter.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ccr.shelter.R;
import com.ccr.shelter.dialog.DatePickerFragment;
import com.ccr.shelter.petData.Pet;
import com.ccr.shelter.viewmodel.PetViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditorActivity extends AppCompatActivity {

    private PetViewModel mPetViewModel;

    private static int RESULT_LOAD_IMAGE = 1;

    private TextInputEditText mNameEditText;

    private TextInputEditText mBreedEditText;

    private TextInputEditText mWeightEditText;

    private Spinner mGenderSpinner;

    private ImageView mImageView;

    private TextInputEditText mBirthDatePicker;

    private TextInputEditText mDetails;

    Bundle extras;

    int id;
    String name;
    private int mGender = 0;
    String breed;
    int weight;
    byte[] imageAsByteArray;
    String birthdate;
    String details;

    final int PIC_CROP = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        verifyStoragePermissions(this);
        setViews();
        setupSpinner();

        mBirthDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDialog();
            }
        });

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
            mBirthDatePicker.setText(selectedPet.getBirthdate());
            mDetails.setText(selectedPet.getDetails());

        }


    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    void setViews() {
        mNameEditText = findViewById(R.id.edit_pet_name);
        mBreedEditText = findViewById(R.id.edit_pet_breed);
        mWeightEditText = findViewById(R.id.edit_pet_weight);
        mGenderSpinner = findViewById(R.id.spinner_gender);
        mImageView = findViewById(R.id.pet_image);
        mBirthDatePicker = findViewById(R.id.edit_pet_birthdate);
        mDetails = findViewById(R.id.edit_pet_details);


    }

    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }

    private void setupDialog(){

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                final String selectedDate = day + "/" + (month + 1) + "/" + year;
                mBirthDatePicker.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void insertPet() {

        getValues();

        Intent replyIntent = new Intent();

        if ((TextUtils.isEmpty(mNameEditText.getText())) || (TextUtils.isEmpty(mBreedEditText.getText())) || (TextUtils.isEmpty(mWeightEditText.getText()))) {
            Toast toastEmptyName = Toast.makeText(this, R.string.errorMessageName, Toast.LENGTH_SHORT);
            toastEmptyName.show();
            setResult(RESULT_CANCELED, replyIntent);

        } else {

            replyIntent.putExtra("Name", name);
            replyIntent.putExtra("Breed", breed);
            replyIntent.putExtra("Gender", mGender);
            replyIntent.putExtra("Date", birthdate);
            replyIntent.putExtra("Weight", weight);
            replyIntent.putExtra("Image", imageAsByteArray);


            setResult(RESULT_OK, replyIntent);
        }

        finish();
    }

    private void updatePet() {
        getValues();
        mPetViewModel.update(new Pet(id, name, breed, mGender, birthdate, weight, details, imageAsByteArray));
        finish();
    }

    private void deletePet() {
        getValues();
        mPetViewModel.delete(new Pet(id, name, breed, mGender, birthdate, weight, details, imageAsByteArray));

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
        details = mDetails.getText().toString();
        birthdate = mBirthDatePicker.getText().toString();


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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
        return stream.toByteArray();
    }

    public void onClickPickImage(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

            ExifInterface exifInterface = null;
            try {
                File pictureFile = new File(picturePath);
                exifInterface = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;
            if (exifInterface != null) {
                orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            }

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;

            }

            mImageView.setImageBitmap(loadedBitmap);
        }

    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}