package com.example.journeyease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    EditText uemail;
    EditText upassword;
     EditText confirm;
    Button register;
    Button login;
    EditText contact;
    Button validate;
    private FirebaseAuth auth;
    String str="";
    String st="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth=FirebaseAuth.getInstance();
        uemail = findViewById(R.id.email);
        upassword=findViewById(R.id.password);
        register = findViewById(R.id.register);
        confirm=findViewById(R.id.password2);
        login=findViewById(R.id.signin);
        contact=findViewById(R.id.phone);
        validate=findViewById(R.id.verify);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ve=new Intent(MainActivity2.this,MainActivity10.class);
                startActivity(ve);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), MainActivity13.class);
                startActivity(i);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = uemail.getText().toString().trim();
                String pass=upassword.getText().toString().trim();
                String con=confirm.getText().toString().trim();
                String ph=contact.getText().toString();
                if (user.isEmpty()){
                    uemail.setError("Email cannot be empty");
                }
                if(pass.isEmpty()) {
                    upassword.setError("Password cannot be empty");
                }
                if(con.isEmpty()){
                    confirm.setError("Please confirm your password");
                }
                if(ph.isEmpty())
                    contact.setError("Phone number cannot be empty");
                else{
                    auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseFirestore db=FirebaseFirestore.getInstance();
                                Map<String,Object> u=new HashMap<>();
                                u.put("mail",user);
                                u.put("phone",ph);
                                db.collection("users").document(user).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(MainActivity2.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity2.this, MainActivity3.class));
                                        }
                                        else
                                            Toast.makeText(MainActivity2.this,"SignUp Failed "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            else{
                                Toast.makeText(MainActivity2.this,"SignUp Failed "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        str = uemail.getText().toString();
        st=contact.getText().toString();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        uemail.setText(str);
        contact.setText(st);
    }

}
