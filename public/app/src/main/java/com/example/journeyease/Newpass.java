package com.example.journeyease;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class Newpass extends AppCompatActivity implements PaymentResultListener
{
    public int totalPayment;
    String[] months = {"0 ","1 month", "3 months", "6 months", "12 months"};
    String[] routes = {"select route","smh-tgv", "GWK-complex", "GWK-Smh", "Complex-TGV","General Pass"};
    Button payment;
    Button cancel;
    String selectedMonth="0";
    String selectedRoute;
    FirebaseAuth auth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpass);

        payment=findViewById(R.id.buttonCalculateTotalPayment);
        Checkout.preload(getApplicationContext());
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db= FirebaseFirestore.getInstance();
                auth=FirebaseAuth.getInstance();
                String uid=auth.getCurrentUser().getEmail();
                Map<String,Object> da=new HashMap<>();
                da.put("Route",selectedRoute);
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
                PaymentNow(String.valueOf(totalPayment));
            }
        });
        cancel=findViewById(R.id.button4);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent b=new Intent(Newpass.this,MainActivity4.class);
                startActivity(b);
            }
        });

        // Get references to the Spinners and TextView
        Spinner spinnerMonths = findViewById(R.id.spinnerMonths);
        Spinner route = findViewById(R.id.spinnerRoute);
        TextView totalPaymentTextView = findViewById(R.id.textViewTotalPayment);

        // Set up ArrayAdapter for spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,  androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, months);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, routes);
        adapter1.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        route.setAdapter(adapter1);




        // Spinner item selection listeners
        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = parent.getItemAtPosition(position).toString();
                calculateAndDisplayTotalPayment(selectedMonth, route.getSelectedItem().toString(), totalPaymentTextView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoute = parent.getItemAtPosition(position).toString();
                calculateAndDisplayTotalPayment(spinnerMonths.getSelectedItem().toString(), selectedRoute, totalPaymentTextView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
     void calculateAndDisplayTotalPayment(String selectedMonth, String selectedRoute, TextView totalPaymentTextView)
    {
        totalPayment = calculateTotalPaymentBasedOnSelection(selectedMonth, selectedRoute);
        totalPaymentTextView.setText("Rs." + totalPayment);
    }

    private int calculateTotalPaymentBasedOnSelection(String selectedMonth, String selectedRoute)
    {
        if (selectedRoute.equals("select route")) {
            return 0;
        }

        else  if (selectedRoute.equals("General Pass")) {
            switch (selectedMonth) {
                case "1 month":
                    return 350;
                case "3 months":
                    return 900;
                case "6 months":
                    return 1200;
                case "12 months":
                    return 2000;
            }
        }
        else
        {
            switch (selectedMonth) {
                case "1 month":
                    return 250;
                case "3 months":
                    return 700;
                case "6 months":
                    return 900;
                case "12 months":
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
        Toast.makeText(Newpass.this,"Payment Success", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(Newpass.this,"Payment Failed", Toast.LENGTH_LONG).show();
    }
}




