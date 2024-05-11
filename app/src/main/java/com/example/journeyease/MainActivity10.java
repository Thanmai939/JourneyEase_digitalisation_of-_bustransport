package com.example.journeyease;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class MainActivity10 extends AppCompatActivity {
    EditText enternumber;
    Button getotpbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main10);

        enternumber = findViewById(R.id.input_mobile_number);
        getotpbutton = findViewById(R.id.buttongetotp);
        ProgressBar progressbar=findViewById(R.id.progressbar_sending_otp);
        getotpbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!enternumber.getText().toString().trim().isEmpty()) {
                    if (enternumber.getText().toString().trim().length() == 10) {
                        progressbar.setVisibility(View.VISIBLE);
                        getotpbutton.setVisibility(View.INVISIBLE);

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();

                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber("+91"+enternumber.getText().toString())       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(MainActivity10.this)             // (optional) Activity for callback binding
                                        // If no activity is passed, reCAPTCHA verification can not be u
                                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                progressbar.setVisibility(View.GONE);
                                                getotpbutton.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                                progressbar.setVisibility(View.GONE);
                                                getotpbutton.setVisibility(View.VISIBLE);
                                                Toast.makeText(MainActivity10.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCodeSent(@NonNull String backotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                progressbar.setVisibility(View.GONE);
                                                getotpbutton.setVisibility(View.VISIBLE);
                                                Intent intent = new Intent(getApplicationContext(), verifyenterotp.class);
                                                String mobile = enternumber.getText().toString();
                                                intent.putExtra("mobile", mobile);
                                                intent.putExtra("backotp", backotp);
                                                startActivity(intent);
                                            }
                                        })
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);

                    } else {
                        Toast.makeText(MainActivity10.this, "Please Enter Correct mobile number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity10.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
