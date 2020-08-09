package com.example.tracc_it;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedsFragmentMod extends Fragment {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText medDose, medSignature;
    /**/     TimePicker timePicker1;
    /**/     Spinner medName;
    /**/     Button modmedsButton;
    /**/     String document;


    private String TAG;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mod_meds, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        medName = view.findViewById(R.id.medName);
        medDose = view.findViewById(R.id.medDose);
        medSignature = view.findViewById(R.id.medSignature);
        modmedsButton = view.findViewById(R.id.modmedsButton);
        timePicker1 = view.findViewById(R.id.timePicker1);
        int hour = timePicker1.getHour();
        int min = timePicker1.getMinute();
        String Time;


        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        CollectionReference Rxref = database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("Rx");


        medName = view.findViewById(R.id.medName);
        List<String> med = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, med);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medName.setAdapter(adapter);

        Rxref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot rx : task.getResult())
                        med.add(rx.getId());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        medName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                document = adapterView.getItemAtPosition(i).toString();

                DocumentReference rxref = Rxref.document(document);

                rxref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot Rx = task.getResult();
                            //textDoctorName.setText(textDoctorName.getText().toString().concat(": " + doc.get("name").toString()));
                            medDose.setText(Rx.get("Rx Dose").toString());
                            medSignature.setText(Rx.get("Rx Signature").toString());

                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        modmedsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRx();
            }
        });


    }

    private void updateRx()
    {

        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Map<String, Object> Rx = new HashMap<>();
        try {
            //doctor.put("name", textDoctorName.getText().toString().trim());
            Rx.put("Rx Dose", medDose.getText().toString().trim());
            Rx.put("Rx Signature", medSignature.getText().toString().trim());
//            Rx.put("Rx Time", Time);


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }



        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("Rx").document(document)
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

