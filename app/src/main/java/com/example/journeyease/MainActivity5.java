package com.example.journeyease;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
    }
    public void Home(View v){
        Intent a=new Intent(MainActivity5.this,MainActivity4.class);
        startActivity(a);
    }
    public void viewprof(View v){
        Intent p=new Intent(MainActivity5.this,MainActivity9.class);
        startActivity(p);
    }
    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity5.this,MainActivity3.class));
        finish();
    }
    public void invite(View v){
        ShareCompat.IntentBuilder.from(MainActivity5.this)
                .setType("text/plain")
                .setChooserTitle("Chooser title")
                .setText("http://play.google.com/store/apps/details?id=JourneyEase" )
                .startChooser();
    }
}