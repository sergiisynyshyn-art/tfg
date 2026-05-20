package com.example.serenum;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        ImageButton btnBack = findViewById(R.id.btnBackPrivacy);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }
    }
}

