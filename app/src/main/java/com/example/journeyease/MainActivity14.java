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

import org.w3c.dom.Text;

public class MainActivity14 extends AppCompatActivity {
    Button elog;
    Button regi;
    EditText username;
    EditText password;
    private FirebaseAuth auth;
    TextView pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main14);
        elog=findViewById(R.id.login);
        username = findViewById(R.id.mail);
        password = findViewById(R.id.pass);
        regi = findViewById(R.id.create);
        auth= FirebaseAuth.getInstance();
        pass=findViewById(R.id.forgot);
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity14.this,MainActivity2.class);
                startActivity(i);
            }
        });
        elog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=username.getText().toString();
                String pass=password.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!pass.isEmpty()){
                        auth.signInWithEmailAndPassword(email,pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseFirestore db=FirebaseFirestore.getInstance();
                                        DocumentReference def=db.collection("Employees").document(email.toString());
                                        def.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Toast.makeText(MainActivity14.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(MainActivity14.this, MainActivity11.class);
                                                        startActivity(i);
                                                    } else
                                                        Toast.makeText(MainActivity14.this, "Not an Employee,Please login as user", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity14.this,"Login Failed",Toast.LENGTH_SHORT).show();
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
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity14.this,forgotPassword.class);
                startActivity(intent);
            }
        });
    }
}