package com.example.tracc_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.inappmessaging.model.Text;

import java.util.HashMap;
import java.util.Map;

public class ModifyMeds extends AppCompatActivity {
    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    U S E R    I N F O R M A T I O N
    /**/    Number medDose, hour, min;
    /**/    Text medName, medSignature;
    /**/    CheckBox checkBoxMed;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T    V A R I A B L E S
    /**/     EditText textMedName, textMedDose, textMedSignature;
    /**/     Button medButton;
    /**/     TimePicker timePicker1;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_meds);
        textMedDose= findViewById(R.id.medDose);
        textMedName = findViewById(R.id.medName);
        textMedSignature = findViewById(R.id.medSignature);
        medButton = findViewById(R.id.medsButton);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        int hour = timePicker1.getHour();
        int min = timePicker1.getMinute();
        checkBoxMed=(CheckBox)findViewById(R.id.checkBoxMed);



        medButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Your medication has been added!", Toast.LENGTH_LONG).show();
                addMeds();

            }
        });

    }
    public void onCheckboxClicked(View view) {
        String msg="";
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBoxMed:
                if (checked)
                    // Put some meat on the sandwich
                    if(checkBoxMed.isChecked()) {
                        msg = "Replace this with calling notification system function";
                        Toast.makeText(this, msg + "are selected",
                                Toast.LENGTH_LONG).show();
                    }
                    else
                        // Remove the meat
                        break;

        }
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
                        startActivity(new Intent(ModifyMeds.this, MainActivity.class));

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