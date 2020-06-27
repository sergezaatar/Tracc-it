package com.example.tracc_it;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPasswordDialog extends AppCompatDialogFragment implements TextWatcher{

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ///*/////      D I A L O G     V A R I A B L E S
    //*/
    /**/    private ForgotPasswordDialogListener listener;  // Listener to interact with LoginActivity
    /**/    private FirebaseAuth mAuth;                     // FirebaseAuth to use firebase functionality
    /**/    private EditText email;                         // Input for email address
    /**/    private boolean valid;                          // Determines if empty or not


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Initialize Firebase authenticator to allow access to Firebase
        mAuth = FirebaseAuth.getInstance();

        // Set valid to start as false, can change if user had already entered
        // their email inside of LoginActivity. See line 52
        valid = false;

        // Create a new builder for the pop-up
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Setting up the Layout of the Pop-up Dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot_pass, null);
        email = view.findViewById(R.id.email_forgot);

        email.addTextChangedListener(this);  // Ensure there is no empty input

        // If the user had already typed in their email in the LoginActivity
        // pull it and present it inside of the EditText
        String login_email = listener.getEmail();
        if ( login_email != null && !login_email.isEmpty() && login_email.contains("@") )
            email.setText(login_email);

        builder.setView(view).setTitle("Send reset password link").setNegativeButton("Cancel", null)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Check if the EditText input is empty
                        if (valid) {
                            mAuth.fetchSignInMethodsForEmail(email.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                            // Check if the provided input exists in Firebase
                                            boolean existing_email = !task.getResult().getSignInMethods().isEmpty();

                                            if (existing_email) {  // Send an email if it is found
                                                mAuth.sendPasswordResetEmail(email.getText().toString());
                                            } else {
                                                String err = "ERROR: " + email.getText().toString() + " not registered. No email sent.";
                                                listener.displayError(err); // Otherwise display error in LoginActivity
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            String err = "ERROR: Cannot be empty. No email sent.";
                            listener.displayError(err);
                        }
                    }
                });

        // Return the builder to be opened from openDialog()
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ForgotPasswordDialog.ForgotPasswordDialogListener) context;
        } catch ( ClassCastException e ){
            throw new ClassCastException(context.toString() + " must implement the dialog");
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ///*/////      D I A L O G    I N T E R F A C E    D E F I N I T I O N
    //*/
    public interface ForgotPasswordDialogListener {
        String getEmail();   // used to retrieve email from LoginActivity
        void displayError(String err);   // Display Toast Error msg in LoginActivity
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ///*/////      T E X T W A T C H   M E T H O D S
    //*/
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String emailInput = email.getText().toString().trim();
        valid = !emailInput.isEmpty();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String emailInput = email.getText().toString().trim();
        valid = !emailInput.isEmpty();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
