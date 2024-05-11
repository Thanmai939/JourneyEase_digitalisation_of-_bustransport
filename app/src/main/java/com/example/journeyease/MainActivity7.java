package com.example.journeyease;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity7 extends AppCompatActivity {

    CardView feedback;
    CardView rateus;
    CardView help;
    CardView aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        feedback = findViewById(R.id.feedback_card);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedbackEmail();
            }
        });

        rateus = findViewById(R.id.rateus);
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRateUsDialog();
            }
        });

        help = findViewById(R.id.help_card);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog();
            }
        });

        aboutus = findViewById(R.id.aboutus);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTubeVideo("https://www.youtube.com/watch?v=Soej-aithJk");
            }
        });
    }

    private void sendFeedbackEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("bodduthanmai.20.cse@gmail.com") +
                "?subject=" + Uri.encode("Feedback") +
                "&body=" + Uri.encode("");
        Uri uri = Uri.parse(uriText);
        intent.setData(uri);
        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No email app installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRateUsDialog() {
        RateUsDialog rateUsDialog = new RateUsDialog(this);
        rateUsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        rateUsDialog.setCancelable(false);
        rateUsDialog.show();
    }

    private void showHelpDialog() {
        Help_dialog helpDialog = new Help_dialog(this);
        helpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        helpDialog.setCancelable(false);
        helpDialog.show();
    }

    private void openYouTubeVideo(String videoUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            intent.putExtra("force_fullscreen", true);  // Optional, if you want to force fullscreen in the YouTube app
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // YouTube app is not installed, open in web browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(intent);
        }
    }
}
