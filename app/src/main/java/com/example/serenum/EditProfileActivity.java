package com.example.serenum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * Pantalla para editar datos personales. Guarda en SharedPreferences y vuelve a ProfileActivity.
 */
public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        EditText etName = findViewById(R.id.etName);
        EditText etEmail = findViewById(R.id.etEmail);
        MaterialButton btnSave = findViewById(R.id.btnSaveProfile);
        ImageButton btnBack = findViewById(R.id.btnBackEdit);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        SharedPreferences prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE);
        String savedName = prefs.getString("profile_name", "");
        String savedEmail = prefs.getString("profile_email", "");
        etName.setText(savedName);
        etEmail.setText(savedEmail);

        btnSave.setOnClickListener(v -> {
            String name = etName.getText() == null ? "" : etName.getText().toString().trim();
            String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();

            prefs.edit().putString("profile_name", name).putString("profile_email", email).apply();
            Toast.makeText(EditProfileActivity.this, "Datos guardados", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

