package com.example.tracc_it;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class VitalsFragment2 extends Fragment {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     TextView textGlucoseLevel, textHeartRate, textOxygenLevel, textDiaBloodPressure, textSysBloodPressure;
    /**/     Spinner datesSpinner;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mod_vitals, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        textGlucoseLevel = view.findViewById(R.id.sugarLevel);
        textHeartRate = view.findViewById(R.id.heartRate);
        textOxygenLevel = view.findViewById(R.id.oxygenLevel);
        textDiaBloodPressure = view.findViewById(R.id.dBloodPressure);
        textSysBloodPressure = view.findViewById(R.id.sBloodPressure);
        datesSpinner = view.findViewById(R.id.datesSpinner);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.dates, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        datesSpinner.setAdapter(arrayAdapter);


    }
}