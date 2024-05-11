package com.example.journeyease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity3 extends AppCompatActivity {

    EditText username;
    EditText password;
    Button login;
    Button regi;
    private FirebaseAuth auth;
    TextView forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        username = findViewById(R.id.mail);
        password = findViewById(R.id.pass);
        regi = findViewById(R.id.create);
        auth= FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        forgot=findViewById(R.id.forpass);
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email=username.getText().toString();
                String pass=password.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!pass.isEmpty()){
                        auth.signInWithEmailAndPassword(email,pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        DocumentReference def = db.collection("users").document(email.toString());
                                        def.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Toast.makeText(MainActivity3.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(MainActivity3.this, MainActivity4.class);
                                                        startActivity(i);
                                                    } else
                                                        Toast.makeText(MainActivity3.this, "Not a user,Please login as employee", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity3.this,"Login Failed",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        password.setError("Password cannot be empty");
                    }
                }else if(email.isEmpty()){
                    username.setError("Email cannot be empty");
                }else{
                    username.setError("Enter valid email");
                }
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent inten=new Intent(MainActivity3.this,forgotPassword.class);
                 startActivity(inten);
            }
        });

    }
}

