package com.ccr.shelter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ccr.shelter.R;
import com.ccr.shelter.activities.EditorActivity;
import com.ccr.shelter.petData.Pet;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetViewHolder> implements Filterable {

    class PetViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView breedView;
        private final TextView ageView;
        private final ImageView genderView;
        private final TextView weightView;
        private final CircleImageView imageView;
        private final ImageView adoptedView;


        private PetViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.name);
            breedView = itemView.findViewById(R.id.breed);
            genderView = itemView.findViewById(R.id.gender);
            weightView = itemView.findViewById(R.id.weight);
            ageView = itemView.findViewById(R.id.age);
            imageView = itemView.findViewById(R.id.image);
            adoptedView = itemView.findViewById(R.id.image_adopted);
        }

        public String getName() {
            return nameView.getText().toString();
        }

    }

    private final LayoutInflater mInflater;
    private List<Pet> mPets;
    public static List<Pet> allPets;

    public PetListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PetListAdapter.PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item, parent, false);
        return new PetViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull PetListAdapter.PetViewHolder holder, int position) {
        if (mPets != null) {

            Pet current = mPets.get(position);
            holder.nameView.setText(current.getName());
            holder.breedView.setText(current.getBreed());
            holder.genderView.setImageResource(genderSelecting(current.getGender()));
            holder.weightView.setText(String.valueOf(current.getWeight()));
            holder.adoptedView.setImageResource(isAdopted(current.getAdoptValue()));
            Bitmap bmp = BitmapFactory.decodeByteArray(current.getImage(), 0, current.getImage().length);
            holder.imageView.setImageBitmap(bmp);
            holder.imageView.setClipToOutline(true);

            try {
                Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(current.getBirthdate());;
                Date date2 = Calendar.getInstance().getTime();

                long yearsOldDate = (date2.getTime() - date1.getTime()) / 86400000 / 365;
                if (yearsOldDate != 0)
                {
                 holder.ageView.setText(yearsOldDate + " años");
                }
                else {
                    holder.ageView.setVisibility(View.GONE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), EditorActivity.class);
                intent.putExtra("Id",current.getId());

                view.getContext().startActivity(intent);
            });
        }
    }


    @Override
    public int getItemCount() {
        if(mPets != null)
            return mPets.size();
        else return 0;
    }


    public void setPets(List<Pet> pets) {
        this.mPets = pets;
        allPets = pets;
        notifyDataSetChanged();
    }

    private int genderSelecting(int gender){
        switch (gender){
            case 1:
                return R.drawable.ic_male;
            case 2:
                return R.drawable.ic_female;
            default:
                return  R.drawable.ic_gender_unknown;
        }
    }

    private int isAdopted(String adopted) {
        switch (adopted){
            case "Sí":
                return R.drawable.ic_adopted;
            default:
                return 0;
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    try {
                        results.count = allPets.size();
                        results.values = allPets;
                    } catch (NullPointerException npe){

                    }

                } else{
                    String searchStr = constraint.toString().toUpperCase();
                    List<Pet> resultsData = new ArrayList<>();

                    for (Pet pet :
                            allPets) {
                        if(pet.getName().toUpperCase().contains(searchStr))
                            resultsData.add(pet);
                    }
                    results.count = resultsData.size();
                    results.values = resultsData;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mPets = (List<Pet>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}



