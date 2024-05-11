package com.example.journeyease;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity15 extends AppCompatActivity {
    FirebaseAuth auth;
    String uid;
    TextView ph,em,s,e,r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main15);
        ph=findViewById(R.id.phone);
        em=findViewById(R.id.email);
        s=findViewById(R.id.from);
        e=findViewById(R.id.to);
        r=findViewById(R.id.route);
        auth=FirebaseAuth.getInstance();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        String aad= getIntent().getStringExtra("aadhar");
        db.collection("users").whereEqualTo("Aadhar",aad).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        ph.setText(document.getString("phone"));
                        em.setText(document.getString("mail"));
                        r.setText(document.getString("Route"));
                        s.setText(document.getString("Start Date"));
                        e.setText(document.getString("End Date"));
                    }

                    FirebaseFirestore dab=FirebaseFirestore.getInstance();
                    Date c= Calendar.getInstance().getTime();
                    SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd-MMM-yyyy").parse(e.getText().toString());
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }

                    if(date.compareTo(c)>0) {
                        Map<String, Object> dap = new HashMap<>();
                        dap.put("Aadhar", aad);
                        dab.collection("pass").document(df.format(c)).set(dap);
                    }
                    else
                        Toast.makeText(MainActivity15.this,"Pass expired",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}