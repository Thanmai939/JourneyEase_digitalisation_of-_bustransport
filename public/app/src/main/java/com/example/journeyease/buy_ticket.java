package com.example.journeyease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class buy_ticket extends AppCompatActivity {
    public int totalPayment;
    public int count;
    EditText num;
    String[] sources = {"select","One day pass", "tgv", "anandapuram", "gudilova", "neelakundeelu", "Sontyam", "Gandigundam", "akkireddipalem", "Pendurthi", "Pingadi", "Sabbavaram", "Devipuram", "Rebaka", "Anakapalli",  "Vepagunta", "Simhachalam", "Gopalapatnam","NAD", "Gajuwaka", "kancharapalem", "Vizag Complex", "Railway Station"};
    ArrayList<String> st=new ArrayList<>(Arrays.asList(sources));
    Button payment;
    String[] Destination = {"select", "tgv", "anandapuram", "gudilova", "neelakundeelu", "Sontyam", "Gandigundam", "akkireddipalem", "Pendurthi", "Pingadi", "Sabbavaram", "Devipuram", "Rebaka", "Anakapalli","Vepagunta", "Simhachalam", "Gopalapatnam", "NAD", "Gajuwaka", "kancharapalem", "Vizag Complex", "Railway Station"};
    Button cancel;
    String selectedMonth = "0";
    String selectedRoute;
    FirebaseFirestore db;
    Spinner spinnerMonths,route;
    TextView totalPaymentTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        payment = findViewById(R.id.buttonCalculateTotalPayment);
        spinnerMonths = findViewById(R.id.spinnerMonths);
        route = findViewById(R.id.spinnerRoute);
        totalPaymentTextView = findViewById(R.id.textViewTotalPayment);
        num=findViewById(R.id.ncount);
        count=Integer.parseInt(num.getText().toString());
        num.addTextChangedListener(new TextWatcher() {
                                       @Override
                                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                       }

                                       @Override
                                       public void onTextChanged(CharSequence s, int start, int before, int coun) {
                                           String c=num.getText().toString();
                                           if(!c.equals("")) {
                                               count = Integer.parseInt(c);
                                               calculateAndDisplayTotalPayment(selectedMonth, selectedRoute, totalPaymentTextView, count);
                                           }
                                       }

                                       @Override
                                       public void afterTextChanged(Editable s) {

                                       }
                                   });
                db = FirebaseFirestore.getInstance();
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c= Calendar.getInstance().getTime();
                SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                    Map<String, Object> da = new HashMap<>();
                                    da.put("From",selectedRoute);
                                    da.put("To",selectedMonth);
                                    da.put("People",count);
                                    db.collection(df.format(c)).add(da);
                                    Toast.makeText(buy_ticket.this,"Ticket issued",Toast.LENGTH_LONG).show();
                        Intent inte=new Intent(buy_ticket.this,MainActivity11.class);
                        startActivity(inte);
                    }
                });
        cancel = findViewById(R.id.button4);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(buy_ticket.this, MainActivity11.class);
                startActivity(b);
            }
        });

        // Get references to the Spinners and TextView


        // Set up ArrayAdapter for spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Destination);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sources);
        adapter1.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        route.setAdapter(adapter1);


        // Spinner item selection listeners
        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = parent.getItemAtPosition(position).toString();
                calculateAndDisplayTotalPayment(selectedMonth, route.getSelectedItem().toString(), totalPaymentTextView,count);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoute = parent.getItemAtPosition(position).toString();
                calculateAndDisplayTotalPayment(spinnerMonths.getSelectedItem().toString(), selectedRoute, totalPaymentTextView,count);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    void calculateAndDisplayTotalPayment(String selectedMonth, String selectedRoute, TextView totalPaymentTextView,int cou) {
        if(selectedRoute.equals("One day pass")) {
            totalPayment = 100 * cou;
        }
        else
        totalPayment = Math.abs((st.indexOf(selectedMonth)-st.indexOf(selectedRoute)))*10*cou;
        totalPaymentTextView.setText("Rs." + totalPayment);
    }

}