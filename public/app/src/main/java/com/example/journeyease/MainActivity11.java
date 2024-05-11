package com.example.journeyease;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity11 extends AppCompatActivity {
    ImageView btn_scan;
    FirebaseAuth auth;
    String uid,route,start,end,name,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);
        btn_scan=findViewById(R.id.scan);
        btn_scan.setOnClickListener(v->{
            scanCode();
        });
    }
    public void scanCode(){
        ScanOptions op= new ScanOptions();
        op.setPrompt("Volume up to flash on");
        op.setBeepEnabled(true);
        op.setOrientationLocked(true);
        op.setCaptureActivity(MainActivity12.class);
        barLauncher.launch(op);
    }
    ActivityResultLauncher<ScanOptions> barLauncher=registerForActivityResult(new ScanContract(),result->{
        if(result.getContents()!=null){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity11.this);
            builder.setTitle("Details");
            builder.setMessage(result.getContents());
            String aad=result.getContents();
            builder.setPositiveButton("Pass Authentication", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Intent inten=new Intent(MainActivity11.this,MainActivity15.class);
                    inten.putExtra("aadhar",aad);
                    startActivity(inten);
                }
            }).show();
            builder.setNeutralButton("Ticket", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent inte=new Intent(MainActivity11.this,Ticket.class);
                    inte.putExtra("aadhar",aad);
                    startActivity(inte);
                }
            }).show();

        }
    });
    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity11.this,MainActivity3.class));
        finish();
    }
    public void nticket(View v){
        Intent i=new Intent(MainActivity11.this,buy_ticket.class);
        startActivity(i);
    }
    public void details(View v){

        Intent d=new Intent(MainActivity11.this,Details.class);
        startActivity(d);
    }
}