package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();

        EditText editText1 = findViewById(R.id.first_name);
        EditText editText2 = findViewById(R.id.middle);
        EditText editText3 = findViewById(R.id.surname);
        EditText editText4 = findViewById(R.id.phone_number);
        EditText editText5 = findViewById(R.id.date_of_birth);
        EditText editText6 = findViewById(R.id.country_details);
        EditText editText7 = findViewById(R.id.city_details);

        Button button = findViewById(R.id.save_details);
        button.setOnClickListener(v -> {
            String first_name = editText1.getText().toString();
            String middle_name = editText2.getText().toString();
            String surname = editText3.getText().toString();
            String phone_number = editText4.getText().toString();
            String date_of_birth = editText5.getText().toString();
            String country = editText6.getText().toString();
            String city = editText7.getText().toString();

            Map<String,Object> details = new HashMap<>();
            details.put("first_name", first_name);
            details.put("middle_name", middle_name);
            details.put("surname", surname);
            details.put("phone_number",phone_number );
            details.put("date_of_birth", date_of_birth);
            details.put("country", country);
            details.put("city", city);

            db.collection("details").add(details)
                    .addOnSuccessListener(documentReference -> Toast.makeText(EditProfileActivity.this, "Successful", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show());
        });
    }
}