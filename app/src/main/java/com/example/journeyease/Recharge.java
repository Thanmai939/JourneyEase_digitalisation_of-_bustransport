package com.example.journeyease;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.WriteResult;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Recharge extends AppCompatActivity implements PaymentResultListener {
    Button deposit;
    Button cancel;
    FirebaseAuth auth;
    FirebaseFirestore db;
    EditText amount;
    int bal=0;
    String baln;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge);
        deposit = findViewById(R.id.pay);
        amount = findViewById(R.id.rechar);
        Checkout.preload(getApplicationContext());
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString() == null)
                    Toast.makeText(Recharge.this, "Please enter amount", Toast.LENGTH_LONG).show();
                else {
                    db = FirebaseFirestore.getInstance();
                    auth = FirebaseAuth.getInstance();
                    String uid = auth.getCurrentUser().getEmail();
                    int amu=Integer.parseInt(amount.getText().toString());
                    DocumentReference dRef = db.collection("users").document(uid);
                    dRef.update("Balance", FieldValue.increment(amu));
                    gotoUrl("https://rzp.io/l/1T09rDHSE");
                    Toast.makeText(Recharge.this,"Payment Success", Toast.LENGTH_LONG).show();
                    //PaymentNow(amount.getText().toString());
                }
            }
        });
        cancel = findViewById(R.id.button4);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(Recharge.this, MainActivity4.class);
                startActivity(b);
            }
        });
    }

    private void gotoUrl(String s) {
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void PaymentNow(String am){
            final Activity activity=this;
            Checkout checkout=new Checkout();
            checkout.setKeyID("rzp_test_mGsEI0Vi8iBRzn");
            checkout.setImage(R.drawable.buslogo);
            double finalAmount;
            finalAmount = Float.parseFloat(am)*100;
            try {
                JSONObject options = new JSONObject();

                options.put("name", "JourneyEase");
                options.put("description", "");
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");

                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("amount", finalAmount);//pass amount in currency subunits
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open(activity, options);

            } catch(Exception e) {
                Log.e(TAG, "Error in starting Razorpay Checkout", e);
            }
        }
        @Override
        public void onPaymentSuccess(String s) {
            db = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            String uid = auth.getCurrentUser().getEmail();
            int amu=Integer.parseInt(amount.getText().toString());
            DocumentReference dRef = db.collection("users").document(uid);
                    dRef.update("Balance", FieldValue.increment(amu));
            Toast.makeText(Recharge.this,"Payment Success", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onPaymentError(int i, String s) {
            Toast.makeText(Recharge.this,"Payment Failed", Toast.LENGTH_LONG).show();
        }
    }

