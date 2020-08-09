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

import java.util.HashMap;
import java.util.Map;



public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private String TAG;

    EditText name, email, phoneNumber, password;
    Button registerButton;
    TextView prompt;

    String Name;
    String Password;
    String Email;
    String PhoneNumber;

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

                Name = name.getText().toString().trim();
                Password = password.getText().toString().trim();
                Email = email.getText().toString().trim();
                PhoneNumber = phoneNumber.getText().toString().trim();


                if (TextUtils.isEmpty((Email))){
                    email.setError("Email is required");
                    return;
                }
                else if (TextUtils.isEmpty((Password)) || Password.length() < 7){
                    password.setError("Password must be more than 6 characters");
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
                    createAccount();
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


    private void createAccount()
    {
        mAuth.createUserWithEmailAndPassword( Email, Password )
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        if ( authResult.getUser() != null )
                        {
                            // First add the name to Firebase
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(Name).build();

                            user.updateProfile(profileChangeRequest);

                            // Add the phone to Firestore
                            database = FirebaseFirestore.getInstance();
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("phone",PhoneNumber);
                            database.collection("users").document(mAuth.getCurrentUser().getEmail())
                                    .set(userInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");

                                            startActivity(new Intent(RegistrationActivity.this, RegistrationActivity2.class));
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

                });
    }

}