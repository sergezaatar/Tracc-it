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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

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

        super.onCreate(savedInstanceState);
        textMedDose = view.findViewById(R.id.medDose);
        textMedName = view.findViewById(R.id.medName);
        textMedSignature = view.findViewById(R.id.medSignature);
        medButton = view.findViewById(R.id.medsButton);
        timePicker1 = view.findViewById(R.id.timePicker1);
        int hour = timePicker1.getHour();
        int min = timePicker1.getMinute();


        medButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Your medication has been added!", Toast.LENGTH_LONG).show();
                addMeds();

            }
        });
    }
    private void addMeds()
    {
        database = FirebaseFirestore.getInstance();

        Map<String, Object> meds = new HashMap<>();
        meds.put("medname", medName);
        meds.put("meddose", medDose);
        meds.put("medsignature", medSignature);
        meds.put("medhour", hour);
        meds.put("medmin", min);



        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .set(meds, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        // After successful upload to database the user has been registered
                        // and can now navigate to the Home page
                        startActivity(new Intent(getView().getContext(), MainActivity.class));

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
