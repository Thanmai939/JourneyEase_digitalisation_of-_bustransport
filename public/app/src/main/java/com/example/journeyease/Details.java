package com.example.journeyease;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.net.MailTo.parse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Details extends AppCompatActivity {
    EditText dat;
    Button det;
    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    String strDate;
    ProgressDialog pd;
    SimpleDateFormat format=new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
    Calendar cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        dat=findViewById(R.id.da);
        det=findViewById(R.id.getDate);
        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userArrayList=new ArrayList<>();
        myAdapter=new MyAdapter(Details.this,userArrayList);
        recyclerView.setAdapter(myAdapter);
        db=FirebaseFirestore.getInstance();
        dat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Details.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                dat.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                cl=Calendar.getInstance();
                                cl.set(year,monthOfYear,dayOfMonth);
                            }
                        },

                        year, month, day);
                datePickerDialog.show();
            }
        });
        det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventChangeListener();
                pd=new ProgressDialog(Details.this);
                pd.setCancelable(false);
                pd.setMessage("Fetching data...");
                pd.show();
            }
        });

            }

    private void EventChangeListener() {
        db.collection(format.format(cl.getTime())).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null || value == null){
                    if(pd.isShowing())
                        pd.dismiss();
                    Toast.makeText(Details.this,"No Data",Toast.LENGTH_LONG).show();
                    return;
                }
                for(DocumentChange dc:value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED)
                        userArrayList.add(dc.getDocument().toObject(User.class));
                    myAdapter.notifyDataSetChanged();
                    if(pd.isShowing())
                        pd.dismiss();
                }
                if(pd.isShowing())
                    pd.dismiss();
            }
        });

    }
}