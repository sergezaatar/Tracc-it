package com.example.tracc_it;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class NotesActivity extends AppCompatActivity {

    private String TAG;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;
    /**/    private RecyclerView recyclerView;


    /////////////////////////////x////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T     V A R I A B L E S
    /**/ EditText notesName, notesSubject, notesBody;
    /**/ Button floatingActionButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_notes);

        //for changing status bar icon colors

        // Initialize Firebase Authenticator
        mAuth = FirebaseAuth.getInstance();

        // Set the overall layout to be activity login XML
        //setContentView(R.layout.fragment_notes);
        // LinearLayout content = (LinearLayout) rootView.findViewById(R.layout.fragment_notes);
        //content.addView(R.layout.fragment_notes);
        View.OnClickListener clicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this, AddNotesFragment.class);
                startActivity(intent);
            }
        };

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(clicker);

        // Initialize all the input variables
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        recyclerView.setAdapter(null);


    }

}