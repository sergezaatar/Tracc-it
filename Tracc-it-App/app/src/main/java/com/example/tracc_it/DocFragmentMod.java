package com.example.tracc_it;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DocFragmentMod extends Fragment {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     Spinner doctorList;
    /**/     Button modifydocButton;

    private String TAG;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mod_doc, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        doctorList = view.findViewById(R.id.doctorList);
        modifydocButton = view.findViewById(R.id.modifydocButton);

        ArrayAdapter<CharSequence  > arrayAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.doctors, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorList.setAdapter(arrayAdapter);


        modifydocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rmmed();
                Toast.makeText(view.getContext(), "Doctor has been successfully modified in the list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
