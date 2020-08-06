package com.example.tracc_it;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class AddNotesFragment extends Fragment {

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

        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // Initialize all the input variables
        TextView notesName = (TextView) view.findViewById(R.id.notesName);
        TextView notesSubject = (TextView) view.findViewById(R.id.notesSubject);
        TextView notesBody = (TextView) view.findViewById(R.id.notesBody);
        Button doneButton = (Button) view.findViewById(R.id.doneButton);

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



