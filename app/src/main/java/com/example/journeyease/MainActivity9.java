package com.example.journeyease;
import static java.util.UUID.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity9 extends AppCompatActivity {
    Button sub;
    EditText etText;
    ImageView ima;
    EditText name;
    Button can;
    private Uri filepath;
    private FirebaseFirestore db;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);
        sub = findViewById(R.id.submit);
        etText = findViewById(R.id.aad);
        ima = findViewById(R.id.imageCode);
        name=findViewById(R.id.fname);
        can=findViewById(R.id.cancels);
        db=FirebaseFirestore.getInstance();
        String uid=auth.getCurrentUser().getEmail();
        DocumentReference dref=db.collection("users").document(uid.toString());

        dref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.getString("Aadhar") != null) {
                    name.setText(value.getString("name"));
                    etText.setText(value.getString("Aadhar"));
                }
            }
            });
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t=new Intent(MainActivity9.this,MainActivity4.class);
                startActivity(t);
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myText = etText.getText().toString().trim();
                String fullname=name.getText().toString();
                boolean result=Aadhar.validateVerhoeff(myText);
                if(result) {
                    MultiFormatWriter mWriter = new MultiFormatWriter();
                    try {
                        BitMatrix mMatrix = mWriter.encode(myText, BarcodeFormat.QR_CODE, 400, 400);
                        BarcodeEncoder mEncoder = new BarcodeEncoder();
                        Bitmap mBitmap = mEncoder.createBitmap(mMatrix);
                        ima.setImageBitmap(mBitmap);
                        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(etText.getApplicationWindowToken(), 0);

                        Map<String, Object> da = new HashMap<>();
                        da.put("Aadhar", myText);
                        da.put("name", fullname);
                        db.collection("users").document(uid).set(da, SetOptions.merge());

                    } catch (WriterException e) {
                        Toast.makeText(MainActivity9.this, "Please enter the aadhar number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(MainActivity9.this,"Please enter valid aadhar number",Toast.LENGTH_LONG).show();
            }
        });
    }
    }

