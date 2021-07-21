package com.example.money_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {

    private TextView signUpText;
    private Button loginButton;
    public User user = User.getInstance();
    private EditText emailText;
    private EditText passwordText;
    public boolean isLogged;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        emailText = (EditText) findViewById(R.id.editTextTextPasswordLogin);
        passwordText = (EditText) findViewById(R.id.editTextTextPasswordLogin);
        signUpText = (TextView) findViewById(R.id.signUpText);
        loginButton = (Button) findViewById(R.id.loginButton);

        /*if (database.getCurrentUser() == null) {
            isLogged = false;
        }*/


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
        String password = passwordText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this,
                    "Enter E-mail and password", Toast.LENGTH_LONG).show();
        } else {
            //TODO make login logic

        }
    }
}