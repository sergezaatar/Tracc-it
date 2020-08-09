package com.example.tracc_it;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import static android.Manifest.permission.CALL_PHONE;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private FirebaseFirestore database;
    DocumentReference userReference;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    U S E R    I N F O R M A T I O N
    /**/    TextView phoneNumber, email, age, height, weight,mdName, mdPhone, mdEmail, name;
    /**/    Button callButton, emailButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Set up view variables
        name = view.findViewById(R.id.welcomeText);
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

        updateProfileInfo();

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

    private void updateProfileInfo() {

        // Update users name
        updateFirebaseInfo();

        // Update datbase info
        userReference = database.collection("users").document(mAuth.getCurrentUser().getEmail());

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    phoneNumber.setText(phoneNumber.getText().toString().concat(": " +
                            doc.getString("phone").replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3")));
                    age.setText(age.getText().toString().concat(": " + doc.get("age").toString()));
                    height.setText(height.getText().toString().concat(": " + doc.getString("height")));
                    weight.setText(weight.getText().toString().concat(": " + doc.get("weight").toString()));

                    updateDoctorInfo();
                }
            }
        });
    }

    private void updateFirebaseInfo() {
        name.setText(name.getText().toString().split(" ")[0]  // Gets "Welcome,"
                .concat(" " + mAuth.getCurrentUser().getDisplayName() + "!")); // adds user's name + "!"
        email.setText(email.getText().toString().concat(": " + mAuth.getCurrentUser().getEmail()));
    }

    private void updateDoctorInfo() {
        userReference.collection("doctor").whereEqualTo("isPCP", true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // This loop should only run once
                for (QueryDocumentSnapshot doctor : task.getResult())
                {
                    mdName.setText(doctor.getId());
                    mdEmail.setText(doctor.getString("email"));
                    mdPhone.setText(doctor.getString("phone").replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));
                }
            }
        });

    }

    public void onCall(View view) {
//        Intent i = new Intent(Intent.ACTION_CALL);
//        i.setData(Uri.parse("tel: 222-222-2222"));
//        startActivity(i);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + mdPhone.getText().toString()));
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);

            startActivity(intent);


        }
    }

}
