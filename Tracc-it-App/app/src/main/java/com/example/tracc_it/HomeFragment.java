package com.example.tracc_it;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import static android.Manifest.permission.CALL_PHONE;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    U S E R    I N F O R M A T I O N
    /**/    TextView phoneNumber, email, age, height, weight,mdName, mdPhone, mdEmail;
    /**/    Button callButton, emailButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        email = view.findViewById(R.id.email);
        age = view.findViewById(R.id.age);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);
        mdName = view.findViewById(R.id.mdName);
        mdPhone = view.findViewById(R.id.mdPhone);
        mdEmail = view.findViewById(R.id.mdEmail);
        callButton = view.findViewById(R.id.callButton);
        emailButton = view.findViewById(R.id.emailButton);
        String Subject = "Appointment Check-In";
        String message = "I am confirming my appointment";


        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Calling!", Toast.LENGTH_LONG).show();
                onCall(v);
            }
        });
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Emailing!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(intent);
            }
        });
    }
    public void onCall(View view) {
//        Intent i = new Intent(Intent.ACTION_CALL);
//        i.setData(Uri.parse("tel: 222-222-2222"));
//        startActivity(i);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: 2142222222"));
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);

            startActivity(intent);


        }
    }

}
