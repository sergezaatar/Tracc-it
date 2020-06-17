package com.example.tracc_it;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.inappmessaging.model.Text;

import java.util.HashMap;
import java.util.Map;



public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private String TAG;

    EditText name, email, phoneNumber, password;
    Button registerButton;
    TextView prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        changeStatusBarColor();
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        registerButton = findViewById(R.id.registerButton);
        prompt = findViewById(R.id.login_prompt);

        prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Name = name.getText().toString().trim();
                String Password = password.getText().toString().trim();
                final String Email = email.getText().toString().trim();
                final String PhoneNumber = phoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty((Email))){
                    email.setError("Email is required");
                    return;
                }
                else if (TextUtils.isEmpty((Password))){
                    password.setError("Password is required");
                    return;
                }
                else if (TextUtils.isEmpty((PhoneNumber))){
                    phoneNumber.setError("Phone Number is required");
                    return;
                }
                else if (TextUtils.isEmpty((Name))){
                    name.setError("Name is required");
                    return;
                }
                else
                    createAccount( Email,  Password, Name, PhoneNumber);
            }
        });


    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }


    private void createAccount(String email, String password, final String name, final String phone)
    {
        mAuth.createUserWithEmailAndPassword( email, password )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( !task.isSuccessful() )
                            Toast.makeText(RegistrationActivity.this, "Sign Up Unsuccessful", Toast.LENGTH_SHORT).show();

                        else
                        {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if ( user != null )
                            {
                                // First add the name to Firebase
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();

                                user.updateProfile(profileChangeRequest);

                                // Add the phone to Firestore
                                database = FirebaseFirestore.getInstance();
                                Map<String, String> userInfo = new HashMap<>();
                                userInfo.put("phone",phone);
                                database.collection("users").document(mAuth.getCurrentUser().getEmail())
                                        .set(userInfo)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
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
                        startActivity(new Intent(RegistrationActivity.this, RegistrationActivity2.class));

                    }
                });
    }

}