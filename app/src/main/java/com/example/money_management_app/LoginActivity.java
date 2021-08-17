package com.example.money_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {

    private TextView signUpText;
    private Button loginButton;
    public User user = User.getInstance();
    private EditText emailText;
    private EditText passwordText;
    public boolean isLogged;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailText = (EditText) findViewById(R.id.textViewEmailLogin);
        passwordText = (EditText) findViewById(R.id.editTextTextPasswordLogin);
        signUpText = (TextView) findViewById(R.id.signUpText);
        loginButton = (Button) findViewById(R.id.loginButton);

        if (mAuth.getCurrentUser() == null) {
            isLogged = false;
        }


        User user = User.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void startLogin() {
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this,
                    "Enter E-mail and password", Toast.LENGTH_LONG).show();
        } else {
            System.out.println(email + " " + password + "\n");
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Success
                        Toast.makeText(LoginActivity.this,
                                "Login Successful", Toast.LENGTH_LONG).show();
                        isLogged = true;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    else {
                        Toast.makeText(LoginActivity.this,
                                "Login Error", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}