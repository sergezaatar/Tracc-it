package com.example.tracc_it;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.NumberFormat;
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
    /**/    private Spinner spinnerGender;
    /**/    private EditText ageEditText,  weightEditText, heightEditText;
    /**/    private EditText mdAddressEditText, mdPhoneEditText, mdEmailEditText, mdNameEditText;
    /**/    private Button registerButton2;

    /////////////////////////////////////////////////////////
    //    ////////////////////////////////////
    ///*/    U S E R    I N F O R M A T I O N
    /**/     String gender;



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
        heightEditText = findViewById(R.id.height);
        mdNameEditText = findViewById(R.id.mdName);
        mdAddressEditText = findViewById(R.id.MD_ADDRESS_EDIT_TEXT);
        mdPhoneEditText = findViewById(R.id.MD_PHONE_EDIT_TEXT);
        mdEmailEditText = findViewById(R.id.MD_EMAIL_EDIT_TEXT);
        weightEditText = findViewById(R.id.weight);
        registerButton2 = findViewById(R.id.registerButton2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(this);

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
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        gender = adapterView.getItemAtPosition(0).toString();
    }

    private void uploadToDatabase()
    {
        try {
            database = FirebaseFirestore.getInstance();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("age", NumberFormat.getInstance().parse(ageEditText.getText().toString().trim()));
            userInfo.put("gender", gender);
            userInfo.put("height", heightEditText.getText().toString().trim());
            userInfo.put("weight", NumberFormat.getInstance().parse(weightEditText.getText().toString().trim()));

            database.collection("users").document(mAuth.getCurrentUser().getEmail())
                    .set(userInfo, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");

                            updateDoctor();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

        }catch (Exception ex) { Log.w(TAG, "Error writing document", ex); }



    }

    private void updateDoctor() {
        database = FirebaseFirestore.getInstance();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("address", mdAddressEditText.getText().toString().trim());
        userInfo.put("phone", mdPhoneEditText.getText().toString().trim());
        userInfo.put("email", mdEmailEditText.getText().toString().trim());
        userInfo.put("isPCP", true);

        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("doctor").document(mdNameEditText.getText().toString().trim())
                .set(userInfo)
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