package com.example.tracc_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VitalsActivity extends AppCompatActivity {
    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    U S E R    I N F O R M A T I O N
    /**/    Number sugarLevel, heartRate, diaBloodPressure,oxygenLevel, sysBloodPressure;
    /**/

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText textSugarLevel, textHeartRate, textOxygenLevel, textDiaBloodPressure, textSysBloodPressure;
    /**/     Button vitalsButton;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);
        textSugarLevel= findViewById(R.id.sugarLevel);
        textHeartRate = findViewById(R.id.heartRate);
        textOxygenLevel = findViewById(R.id.oxygenLevel);
        textDiaBloodPressure = findViewById(R.id.diaBloodPressure);
        textSysBloodPressure = findViewById(R.id.sysBloodPressure);
        vitalsButton = findViewById(R.id.vitalsButton);

        vitalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVitals();

            }
        });

    }
    private void addVitals()
    {
        database = FirebaseFirestore.getInstance();

        Map<String, Object> vitals = new HashMap<>();
        vitals.put("sugarlevel", sugarLevel);
        vitals.put("diabloodpressure", diaBloodPressure);
        vitals.put("sysbloodpressure", sysBloodPressure);
        vitals.put("oxygenlevel", oxygenLevel);
        vitals.put("heartrate", heartRate);

        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .set(vitals, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        // After successful upload to database the user has been registered
                        // and can now navigate to the Home page
                        startActivity(new Intent(VitalsActivity.this, MainActivity.class));

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