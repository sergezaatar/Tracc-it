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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocFragmentMod extends Fragment {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText textDoctorName, textDoctorEmail, textDoctorPhone, textDoctorAddress;
    /**/     Button modifydocButton;
    /**/     Spinner doctorSpinner;
    /**/     String document;

    private String TAG;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mod_doc, container, false);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        //textDoctorName = view.findViewById(R.id.docName);
        textDoctorEmail = view.findViewById(R.id.docEmail);
        textDoctorPhone = view.findViewById(R.id.docPhone);
        textDoctorAddress = view.findViewById(R.id.docAddress);
        modifydocButton = view.findViewById(R.id.modifydocButton);

        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        CollectionReference doctorRef = database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("doctor");


        doctorSpinner = view.findViewById(R.id.doctorSpinner);
        List<String> doctor = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, doctor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(adapter);

        doctorRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot doc : task.getResult())
                        doctor.add(doc.getId());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                document = adapterView.getItemAtPosition(i).toString();

                DocumentReference docRef = doctorRef.document(document);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            //textDoctorName.setText(textDoctorName.getText().toString().concat(": " + doc.get("name").toString()));
                            textDoctorEmail.setText(doc.get("email").toString());
                            textDoctorPhone.setText(doc.get("phone").toString());
                            textDoctorAddress.setText(doc.get("address").toString());
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        modifydocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDoctor();
            }
        });


    }

    private void updateDoctor()
    {

        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        Map<String, Object> doctor = new HashMap<>();
        try {
            //doctor.put("name", textDoctorName.getText().toString().trim());
            doctor.put("email", textDoctorEmail.getText().toString().trim());
            doctor.put("phone", textDoctorPhone.getText().toString().trim());
            doctor.put("address", textDoctorAddress.getText().toString().trim());
            //doctor.put("isPCP", false);
            //doctor.put("heartrate", NumberFormat.getInstance().parse(textHeartRate.getText().toString().trim()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }



        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("doctor").document(document)
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