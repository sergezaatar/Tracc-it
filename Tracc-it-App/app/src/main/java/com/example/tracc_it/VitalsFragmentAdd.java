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

public class VitalsFragmentAdd extends Fragment {
    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText textGlucoseLevel, textHeartRate, textOxygenLevel, textDiaBloodPressure, textSysBloodPressure;
    /**/     Button vitalsButton;

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
        textGlucoseLevel= view.findViewById(R.id.sugarLevel);
        textHeartRate = view.findViewById(R.id.heartRate);
        textOxygenLevel = view.findViewById(R.id.oxygenLevel);
        textDiaBloodPressure = view.findViewById(R.id.diaBloodPressure);
        textSysBloodPressure = view.findViewById(R.id.sysBloodPressure);
        vitalsButton = view.findViewById(R.id.vitalsButton);

        vitalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVitals();
            }
        });
    }

    private void addVitals()
    {
        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Map<String, Object> vitals = new HashMap<>();
        try {
            vitals.put("glucoselevel", NumberFormat.getInstance().getInstance().parse(textGlucoseLevel.getText().toString().trim()));
            vitals.put("diabloodpressure", NumberFormat.getInstance().getInstance().parse(textDiaBloodPressure.getText().toString().trim()));
            vitals.put("sysbloodpressure", NumberFormat.getInstance().getInstance().parse(textSysBloodPressure.getText().toString().trim()));
            vitals.put("oxygenlevel", NumberFormat.getInstance().getInstance().parse(textOxygenLevel.getText().toString().trim()));
            vitals.put("heartrate", NumberFormat.getInstance().getInstance().parse(textHeartRate.getText().toString().trim()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        // Keep track of vitals per day
        String date = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm", Locale.getDefault()).format(new Date());

        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("vitals").document(date)
                .set(vitals)
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