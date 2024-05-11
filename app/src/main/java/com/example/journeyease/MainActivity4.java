package com.example.journeyease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

    }
    public void profile(View v){
         Intent i=new Intent(MainActivity4.this,MainActivity5.class);
         startActivity(i);
    }
    public void settings(View v){
        Intent in=new Intent(MainActivity4.this,MainActivity7.class);
        startActivity(in);
    }
    public void renew(View v){
        Intent r=new Intent(MainActivity4.this,MainActivity8.class);
        startActivity(r);
    }
    public void Code(View v){
        Intent c=new Intent(MainActivity4.this,QR.class);
        startActivity(c);
    }
    public void pass(View v){
        Intent p=new Intent(MainActivity4.this,Renew.class);
        startActivity(p);
    }
    public void recharge(View v){
        Intent re=new Intent(MainActivity4.this,Recharge.class);
        startActivity(re);

    }


}