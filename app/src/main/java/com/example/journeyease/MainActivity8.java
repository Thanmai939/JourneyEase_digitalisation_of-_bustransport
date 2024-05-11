package com.example.journeyease;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class MainActivity8 extends AppCompatActivity {

    CardView old_pass;
    CardView new_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);

        old_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to abc.xml (assuming it's another activity)
                Intent intent = new Intent(MainActivity8.this, Oldpass.class);
                startActivity(intent);
            }
        });

        new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to abc.xml (assuming it's another activity)
                Intent intent;
                intent = new Intent(MainActivity8.this, Newpass.class);
                startActivity(intent);
            }
        });
    }
}
