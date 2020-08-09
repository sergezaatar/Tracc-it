package com.example.tracc_it;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DocFragmentAdd extends Fragment {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText textDoctorName, textDoctorEmail, textDoctorPhone, textDoctorAddress;
    /**/     Button adddocButton;
    /**/

    private String TAG;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_doc, container, false);
    }




    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        textDoctorName = view.findViewById(R.id.docName);
        textDoctorEmail = view.findViewById(R.id.docEmail);
        textDoctorPhone = view.findViewById(R.id.docPhone);
        textDoctorAddress = view.findViewById(R.id.docAddress);

        adddocButton = view.findViewById(R.id.adddocButton);

        adddocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDoctor();
            }
        });
    }

    private void addDoctor()
    {
        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Map<String, Object> doctor = new HashMap<>();
        try {
            doctor.put("name", textDoctorName.getText().toString().trim());
            doctor.put("email", textDoctorEmail.getText().toString().trim());
            doctor.put("phone", textDoctorPhone.getText().toString().trim());
            doctor.put("address", textDoctorAddress.getText().toString().trim());
            doctor.put("isPCP", false);
            //doctor.put("heartrate", NumberFormat.getInstance().parse(textHeartRate.getText().toString().trim()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }



        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("doctor").document(textDoctorName.getText().toString().trim())
                .set(doctor)
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