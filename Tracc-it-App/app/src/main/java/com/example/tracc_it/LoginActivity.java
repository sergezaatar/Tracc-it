package com.example.tracc_it;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity implements ForgotPasswordDialog.ForgotPasswordDialogListener {

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    F I R E    B A S E    V A R I A B L E S
    /**/    private FirebaseAuth mAuth;

    /////////////////////////////////////////////////////////
    ////////////////////////////////////
    ///*/    I N P U T     V A R I A B L E S
    /**/    EditText email;
    /**/    EditText password;
    /**/    CircularProgressButton button;
    /**/    TextView forgotPassword;
    /**/    TextView newUser;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // Initialize Firebase Authenticator
        mAuth = FirebaseAuth.getInstance();

        // Set the overall layout to be activity login XML
        setContentView(R.layout.activity_login);

        // Initialize all the input variables
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.cirLoginButton);
        forgotPassword = findViewById(R.id.forgot_textview);
        newUser = findViewById(R.id.newuser_prompt);

        // Listener for the new user text prompt, switches to Registration
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        // Opens dialog to send forgot password email, doesn't work properly right now
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        // Verifies if the EditTexts are populated
        // if so calls the signIn() method
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()) {
                    email.requestFocus();
                    email.setError("Email required");
                }
                else if ( password.getText().toString().isEmpty() ) {
                    password.setError("Password required");
                    password.requestFocus();
                }
                else
                    signIn(email.getText().toString(), password.getText().toString() );
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ///*/////      H E L P E R    M E T H O D S
    //*/

    // Opens the dialog for Forgot Password
    private void openDialog() {
        ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
        forgotPasswordDialog.show(getSupportFragmentManager(), "Forgot password");
    }

    // Authenticates a Firebase User and then switches to Home page on MainActivity
    private void signIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ///*/////      I N T E R F A C E    M E T H O D S
    //*/
    @Override
    public String getEmail() {
        return email.getText().toString();
    }
}