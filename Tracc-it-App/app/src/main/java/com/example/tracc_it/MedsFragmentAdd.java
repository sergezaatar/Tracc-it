package com.example.tracc_it;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.concat;

public class MedsFragmentAdd extends Fragment {
    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    U S E R    I N F O R M A T I O N
    /**/    Number medDose, hour, min;
    /**/    String medName, medSignature;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText textMedName, textMedDose, textMedSignature;
    /**/     Button medButton, editButton;
    /**/     TimePicker timePicker1;

    private String TAG;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_meds, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String am_pm;
        super.onCreate(savedInstanceState);
        textMedDose = view.findViewById(R.id.medDose);
        textMedName = view.findViewById(R.id.medName);
        textMedSignature = view.findViewById(R.id.medSignature);
        medButton = view.findViewById(R.id.medsButton);
        timePicker1 = view.findViewById(R.id.timePicker1);
        int hour = timePicker1.getHour();
        int min = timePicker1.getMinute();
        if(hour > 12) {
            am_pm = "PM";
            hour = hour - 12;
        }
        else
        {
            am_pm="AM";
        }
        String time = hour + ":" + min +" "+ am_pm;



        medButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Your medication has been added!", Toast.LENGTH_LONG).show();
                addMeds(time);

            }
        });
    }
    private void addMeds(String time)
    {
        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Map<String, Object> Rx = new HashMap<>();
        try {
            Rx.put("Rx Name", textMedName.getText().toString().trim());
            Rx.put("Rx Dose", textMedDose.getText().toString().trim());
            Rx.put("Rx Signature", textMedSignature.getText().toString().trim());
            Rx.put("Rx Time", time );

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }



        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("Rx").document(textMedName.getText().toString().trim())
                .set(Rx)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        // After successful upload to database the user has added a doctor
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
