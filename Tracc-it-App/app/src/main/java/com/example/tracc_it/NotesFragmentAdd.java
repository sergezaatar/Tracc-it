package com.example.tracc_it;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class NotesFragmentAdd extends Fragment implements AdapterView.OnItemSelectedListener{
    private String TAG;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T     V A R I A B L E S
    /**/ EditText notesName, notesBody;
    /**/ Button doneButton;
    /**/ Spinner noteCategory;

    String category;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        notesName = view.findViewById(R.id.noteName);
        notesBody = view.findViewById(R.id.notesBody);
        doneButton = view.findViewById(R.id.doneButton);
        noteCategory = view.findViewById(R.id.categorySpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        noteCategory.setAdapter(adapter);
        noteCategory.setOnItemSelectedListener(this);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToDatabase();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void uploadToDatabase() {
        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Note content
        Map<String, Object> notes = new HashMap<>();
        notes.put("category", category);
        notes.put("content", notesBody.getText().toString().trim());

        // Upload to database by noteName
        database.collection("users").document(mAuth.getCurrentUser().getEmail())
                .collection("notes").document(notesName.getText().toString().trim())
                .set(notes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        // After successful upload to database the user has added a note
                        // Go to the Notes View page
                        Class fragmentClass = NotesFragmentView.class;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        category = adapterView.getItemAtPosition(0).toString();
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



