package com.example.tracc_it;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MedsFragmentRemove extends Fragment {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     Spinner MedName;
    /**/     Button removemedsButton;

    private String TAG;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rm_meds, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         MedName = view.findViewById(R.id.MedName);
         removemedsButton = view.findViewById(R.id.removemedsButton);

        ArrayAdapter<CharSequence  > arrayAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.numbers, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MedName.setAdapter(arrayAdapter);


        removemedsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rmmed();
                Toast.makeText(view.getContext(), "Medication has been Successfully Removed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
