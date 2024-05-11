package com.example.journeyease;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Renew extends AppCompatActivity {
    TextView n,p,s,r,t;
    ImageView img;
    FirebaseAuth auth;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew);
        img=findViewById(R.id.imga);
        n=findViewById(R.id.text1);
        p=findViewById(R.id.text2);
        r=findViewById(R.id.text3);
        s=findViewById(R.id.text4);
        t=findViewById(R.id.text5);
        auth= FirebaseAuth.getInstance();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        uid=auth.getCurrentUser().getEmail();
        DocumentReference dref=db.collection("users").document(uid.toString());

        dref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String myText = value.getString("Aadhar");
                if (myText == null)
                    n.setText("Please update your profile");
                else {
                    n.setText(value.getString("name"));
                    if (value.getString("Route") == null)
                        r.setText("No Pass");
                    else {
                        s.setText(value.getString("Start Date"));
                        t.setText(value.getString("End Date"));
                        r.setText(value.getString("Route"));
                        p.setText(value.getString("phone"));
                    }
                    MultiFormatWriter mWriter = new MultiFormatWriter();
                    try {
                        BitMatrix mMatrix = mWriter.encode(myText, BarcodeFormat.QR_CODE, 400, 400);
                        BarcodeEncoder mEncoder = new BarcodeEncoder();
                        Bitmap mBitmap = mEncoder.createBitmap(mMatrix);
                        img.setImageBitmap(mBitmap);
                        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(img.getApplicationWindowToken(), 0);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}