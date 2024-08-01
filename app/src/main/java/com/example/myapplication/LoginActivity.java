package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_btn);
        Button googleButton = findViewById(R.id.google);
        Button appleButton = findViewById(R.id.apple);
        Button registrationButton = findViewById(R.id.register);
        TextView forgotPasswordTextView = findViewById(R.id.forgot);

        loginButton.setOnClickListener(view -> {
            String email_field = email.getText().toString();
            String password_field = password.getText().toString();

            if(email_field.isEmpty()||password_field.isEmpty()){
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
            else{
                loginUser(email_field,password_field);
            }
        });

        googleButton.setOnClickListener(view -> {

        });

        appleButton.setOnClickListener(view -> {

        });

        forgotPasswordTextView.setOnClickListener(view -> {

        });

        registrationButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(),RegisterActivity.class);
            view.getContext().startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
            else{
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void ShowHidePass(View view) {

        if(view.getId()==R.id.pass_btn){
            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.baseline_password_24);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.baseline_password_24);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

}