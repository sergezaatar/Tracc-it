package com.example.tracc_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotesFragmentView extends Fragment implements AdapterView.OnItemSelectedListener {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
            private FirebaseFirestore database;


    /////////////////////////////x////////////////////////////
    ////////////////////////////////////
    ///*/    D I S P L A Y    V A R I A B L E S
    /**/    Spinner subjectSpinner;
    /**/    Spinner noteNameSpinner;
    /**/    TextView note;
    String category;
    private CollectionReference notesRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_notes, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up firebase stuff
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        
        // Set up display variables
        subjectSpinner = view.findViewById(R.id.subjectSpinner);
        noteNameSpinner = view.findViewById(R.id.noteNameSpinner);
        note = view.findViewById(R.id.noteContent);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        subjectSpinner.setAdapter(adapter);
        subjectSpinner.setOnItemSelectedListener(this);

        
        
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = adapterView.getItemAtPosition(i).toString();
        clearNoteContent();
        updateNoteContents();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        category = adapterView.getItemAtPosition(0).toString();
        clearNoteContent();
        updateNoteContents();
    }

    private void clearNoteContent() {
        note.setText("No notes found in " + category);
    }

    private void updateNoteContents() {

        notesRef = database.collection("users").document(mAuth.getCurrentUser().getEmail()).collection("notes");
        List<String> notes = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, notes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noteNameSpinner.setAdapter(adapter);

        notesRef.whereEqualTo("category",category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())
                        notes.add(doc.getId());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        noteNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String noteName = adapterView.getItemAtPosition(i).toString();

                DocumentReference docRef = notesRef.document(noteName);

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot doc = task.getResult();
                            note.setText(doc.getString("content"));
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }
}
