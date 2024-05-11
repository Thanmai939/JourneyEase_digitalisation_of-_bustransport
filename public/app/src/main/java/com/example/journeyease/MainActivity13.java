package com.example.journeyease;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity13 extends AppCompatActivity {
    Button elogin;
    Button ulog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main13);
        elogin=findViewById(R.id.login);
        ulog=findViewById(R.id.ulogin);
        elogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity13.this,MainActivity14.class);
                startActivity(i);
            }
        });
        ulog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ni=new Intent(MainActivity13.this,MainActivity3.class);
                startActivity(ni);
            }
        });
    }
}