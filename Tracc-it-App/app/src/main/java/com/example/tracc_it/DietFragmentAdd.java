package com.example.tracc_it;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DietFragmentAdd extends Fragment {
    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText textNameOfMeal, textNumOfCalories,textAddNotes;
    /**/     Button mealButton;

    private String TAG;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_vitals, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        //
        textNameOfMeal = view.findViewById(R.id.nameOfMeal);
        textNumOfCalories=view.findViewById(R.id.numOfCal);
        textAddNotes= view.findViewById(R.id.addNotes);
        mealButton = view.findViewById(R.id.mealSubmitButton);

        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeal();
            }
        });
    }

    private void addMeal()
    {
        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Map<String, Object> meals = new HashMap<>();
        try {
            meals.put("typeofmeal", NumberFormat.getInstance().parse(textNameOfMeal.getText().toString().trim()));
            meals.put("numofcal", NumberFormat.getInstance().parse(textNumOfCalories.getText().toString().trim()));
            meals.put("additionalnotes", NumberFormat.getInstance().parse(textAddNotes.getText().toString().trim()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        // Keep track of vitals per day
        String date = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm", Locale.getDefault()).format(new Date());

        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("meals").document(date)
                .set(meals)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        // After successful upload to database the user has logged their vitals
                        // Set the home page fragment to be viewed first
                        Class fragmentClass = HomeFragment.class;
                        Fragment fragment = null;
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                            // Insert the fragment by replacing any existing fragment
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

}