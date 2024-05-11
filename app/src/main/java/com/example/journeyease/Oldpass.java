package com.example.journeyease;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Oldpass extends AppCompatActivity implements PaymentResultListener {
    private TextView textViewRoute;
    Button cancel;
    String proute="";
    public int totalPayment;
    Button payment;
    String selectedMonth="0";
    FirebaseAuth auth;
    FirebaseFirestore db;
    String[] months = {"0 ", "1 month", "3 months", "6 months", "12 months"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oldpass);

        Checkout.preload(getApplicationContext());

        payment=findViewById(R.id.buttonCalculateTotalPayment);

        cancel=findViewById(R.id.button4);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent b=new Intent(Oldpass.this,MainActivity4.class);
                startActivity(b);
            }
        });

        // Get references to the Spinners and TextView
        Spinner spinnerMonths = findViewById(R.id.spinnerMonths);
        TextView totalPaymentTextView = findViewById(R.id.textViewTotalPayment);

        // Set up ArrayAdapter for spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, months);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(adapter);

        textViewRoute = findViewById(R.id.textViewRoute2);

        // Assume you have a method to get the route value from the user's previous bus pass
        String userPreviousRoute = getUserPreviousRoute();

        // Set the text of the TextView with the route value

        // Spinner item selection listeners
        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = parent.getItemAtPosition(position).toString();
                calculateAndDisplayTotalPayment(selectedMonth, userPreviousRoute, totalPaymentTextView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db= FirebaseFirestore.getInstance();
                auth= FirebaseAuth.getInstance();
                String uid=auth.getCurrentUser().getEmail();
                Map<String,Object> da=new HashMap<>();
                Date c= Calendar.getInstance().getTime();
                SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                da.put("Start Date",df.format(c));
                Calendar cal=Calendar.getInstance();
                int x;
                switch (selectedMonth) {
                    case "1 month":
                        x = 1;
                        break;
                    case "3 months":
                        x = 3;
                        break;
                    case "6 months":
                        x = 6;
                        break;
                    case "12 months":
                        x = 12;
                        break;
                    default:
                        x=0;
                        break;
                }
                cal.add(Calendar.DAY_OF_MONTH,x*30);
                da.put("End Date",df.format(cal.getTime()));
                db.collection("users").document(uid).set(da, SetOptions.merge());
                gotoUrl("https://rzp.io/l/1T09rDHSE");
                //PaymentNow(String.valueOf(totalPayment));
            }
        });
    }

    private String getUserPreviousRoute() {
        // Implement the logic to get the route value from the user's previous bus pass
        // You may retrieve it from SharedPreferences, a database, or any other storage mechanism
        // For example:
        // SharedPreferences sharedPreferences = getSharedPreferences("bus_pass_data", MODE_PRIVATE);
        // return sharedPreferences.getString("route", "DefaultRoute");
        db= FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();
        String uid=auth.getCurrentUser().getEmail();

        DocumentReference dref=db.collection("users").document(uid.toString());

        dref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                proute=value.getString("Route");
                textViewRoute.setText(proute);
            }
            });
        return proute;
    }

     void calculateAndDisplayTotalPayment(String selectedMonth, String selectedRoute, TextView totalPaymentTextView) {
        totalPayment = calculateTotalPaymentBasedOnSelection(selectedMonth, selectedRoute);
        totalPaymentTextView.setText("Rs." + totalPayment);
    }

    private int calculateTotalPaymentBasedOnSelection(String selectedMonth, String selectedRoute) {
        if (selectedRoute.equals("select route")) {
            return 0;
        } else if (selectedRoute.equals("General Pass")) {
            if (selectedMonth.equals("1 month")) {
                return 350;
            } else if (selectedMonth.equals("3 months")) {
                return 900;
            } else if (selectedMonth.equals("6 months")) {
                return 1200;
            } else if (selectedMonth.equals("12 months")) {
                return 2000;
            }
        } else {
            if (selectedMonth.equals("1 month")) {
                return 250;
            } else if (selectedMonth.equals("3 months")) {
                return 700;
            } else if (selectedMonth.equals("6 months")) {
                return 900;
            } else if (selectedMonth.equals("12 months")) {
                return 1000;
            }
        }
        return 0;
    }
    private void PaymentNow(String amount){
        final Activity activity=this;
        Checkout checkout=new Checkout();
        checkout.setKeyID("rzp_test_mGsEI0Vi8iBRzn");
        checkout.setImage(R.drawable.ic_launcher_background);
        double finalAmount;
        finalAmount = Float.parseFloat(amount)*100;
        try {
            JSONObject options = new JSONObject();

            options.put("name", "JourneyEase");
            options.put("description", "");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //from response of step 3.
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
        Toast.makeText(Oldpass.this,"Payment Success", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(Oldpass.this,"Payment Failed", Toast.LENGTH_LONG).show();
    }
    private void gotoUrl(String s) {
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}

