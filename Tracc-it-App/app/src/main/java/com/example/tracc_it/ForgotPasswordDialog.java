package com.example.tracc_it;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class ForgotPasswordDialog extends AppCompatDialogFragment {

    private ForgotPasswordDialogListener listener;
    private FirebaseAuth mAuth;
    private EditText email;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Initialize Firebase authenticator to allow access to Firebase
        mAuth = FirebaseAuth.getInstance();

        // Create a new builder for the pop-up
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Setting up the Layout of the Pop-up Dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot_pass, null);
        email = view.findViewById(R.id.email_forgot);

        // If the user had already typed in their email in the LoginActivity
        // pull it and present it inside of the EditText
        String login_email = listener.getEmail();
        if ( login_email != null && !login_email.isEmpty() && login_email.contains("@") )
            email.setText(login_email);


        builder.setView(view).setTitle("Send reset password link").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        }).setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if ( email.getText().toString() != null ) {
                            mAuth.fetchSignInMethodsForEmail(email.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                            boolean existing_email = !task.getResult().getSignInMethods().isEmpty();

                                            if (existing_email) {
                                                mAuth.sendPasswordResetEmail(email.getText().toString());
                                                dismiss();
                                            }
                                        }
                                    });
                        }
                        else {
                            email.requestFocus();
                            email.setError("Enter an email");
                            return;
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

    // Defining the interface and its methods
    // getEmail will be used to get the email if entered in LoginActivity
    public interface ForgotPasswordDialogListener {
        String getEmail();
    }
}
