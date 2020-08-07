package com.example.tracc_it;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class NotesFragmentAdd extends Fragment {
    private String TAG;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T     V A R I A B L E S
    /**/ EditText notesName, notesSubject, notesBody;
    /**/ Button doneButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors


        // Initialize Firebase Authenticator
        mAuth = FirebaseAuth.getInstance();

        // Set the overall layout to be activity login XML
        //setContentView(R.layout.fragment_notes);
        // LinearLayout content = (LinearLayout) rootView.findViewById(R.layout.fragment_notes);
        //content.addView(R.layout.fragment_notes);

        View view = inflater.inflate(R.layout.fragment_add_notes, container, false);

        // Initialize all the input variables
        notesName =  view.findViewById(R.id.notesName);
        notesSubject = view.findViewById(R.id.notesSubject);
        notesBody = view.findViewById(R.id.notesBody);
        doneButton = view.findViewById(R.id.doneButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToDatabase();

            }
        });
        return view;

    }

    private void uploadToDatabase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String, Object> notes = new HashMap<>();
        notes.put("notesName", notesName);
        notes.put("notesSubject", notesSubject);
        notes.put("notesBody", notesBody);


        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .set(notes, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        // After successful upload to database the user has been registered
                        // and can now navigate to the Home page

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










////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////
///*/////      I N T E R F A C E    M E T H O D S
//*/
//    public String getNotes() {
//        notesBody.getText().toString();
//        notesSubject.getText().toString();
//        return notesName.getText().toString();
//    }



