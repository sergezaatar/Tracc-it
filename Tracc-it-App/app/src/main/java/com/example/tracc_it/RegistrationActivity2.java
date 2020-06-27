package com.example.tracc_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;


    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/    private Spinner spinnerGender, heightFtEditText, heightInEditText;
    /**/    private EditText ageEditText, mdNameEditText, weightEditText;
    /**/    private EditText annualDate, lastDate;

    /////////////////////////////////////////////////////////
    //    ////////////////////////////////////
    ///*/    U S E R    I N F O R M A T I O N
    /**/    Number age;
    /**/    DecimalFormat weight;
    /**/    String mdName;
    /**/    String gender, height;
            Date annualCheckupDate, lastAppointmentDate;
            Button registerButton2;


    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();

        // Initialize the Activity XML layout
        setContentView(R.layout.activity_registration2);

        // Initialize the instance of Firebase
        // in order to validate if a user can access database
        mAuth = FirebaseAuth.getInstance();

        // Initialize the input variables with findViewById()
        ageEditText = findViewById(R.id.age);
        spinnerGender = findViewById(R.id.spinnerGender);
        heightFtEditText = findViewById(R.id.heightFeet);
        heightInEditText = findViewById(R.id.heightInches);
        mdNameEditText = findViewById(R.id.mdName);
        annualDate = findViewById(R.id.annualCheckupDate);
        lastDate = findViewById(R.id.lastAppointmentDate);
        weightEditText = findViewById(R.id.weight);
        registerButton2 = findViewById(R.id.registerButton2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(this);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFeet = ArrayAdapter.createFromResource(this, R.array.feet_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFeet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        heightFtEditText.setAdapter(adapterFeet);
        heightFtEditText.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterInches = ArrayAdapter.createFromResource(this, R.array.inches_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterInches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        heightInEditText.setAdapter(adapterInches);
        heightInEditText.setOnItemSelectedListener(this);

        registerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToDatabase();

            }
        });

    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender = adapterView.getItemAtPosition(i).toString();
        height = adapterView.getItemAtPosition(i).toString()+'.'+ adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        gender = adapterView.getItemAtPosition(0).toString();
        height = adapterView.getItemAtPosition(0).toString()+'.'+ adapterView.getItemAtPosition(0).toString();

    }

    private void uploadToDatabase()
    {
        database = FirebaseFirestore.getInstance();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("age", age);
        userInfo.put("gender", gender);
        userInfo.put("height", height);
        userInfo.put("weight", weight);
        userInfo.put("pcp", mdName);
        userInfo.put("checkup", annualCheckupDate);
        userInfo.put("lastAppt",lastAppointmentDate );

        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        // After successful upload to database the user has been registered
                        // and can now navigate to the Home page
                        startActivity(new Intent(RegistrationActivity2.this, MainActivity.class));

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