package com.example.tracc_it;

import android.app.AlertDialog;
import android.app.Dialog;
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
        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot_pass, null);
        email = view.findViewById(R.id.email_forgot);

        String login_email = listener.getEmail();

        if ( login_email != null && !login_email.isEmpty() && login_email.contains("@") )
            email.setText(login_email);


        builder.setView(view).setTitle("Send reset password link").setNegativeButton("Cancel", null)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.fetchSignInMethodsForEmail(email.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                        boolean existing_email = !task.getResult().getSignInMethods().isEmpty();

                                        if ( existing_email )
                                            mAuth.sendPasswordResetEmail(email.getText().toString());
                                    }
                                });

                    }
                });

        return builder.create();
    }

    public interface ForgotPasswordDialogListener {
        String getEmail();
    }
}
