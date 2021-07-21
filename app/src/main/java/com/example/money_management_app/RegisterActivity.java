package com.example.money_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {

    private TextView signInText;

    private EditText emailText;
    private EditText passwordText;
    private EditText passwordConfirmation;

    private AppCompatButton registerButton;

    public User user = User.getInstance();

    FirebaseAuth mAuth;


    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference ref = database.getReference("users");

    void addNewUser(String email) {
        String username = email.split("@")[0];
        user.resetUser();
        user.setEmail(email);
        user.setName(username);
        DatabaseReference localRef = ref.child(username);
        ref.child(username).setValue(user);

    }


    private void register() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String password2 = passwordConfirmation.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))  {
            Toast.makeText(RegisterActivity.this,
                    "Enter E-mail and password!", Toast.LENGTH_LONG).show();
        } else if (password.equals(password2) == false) {
            Toast.makeText(RegisterActivity.this,
                    "Passwords do not match!", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,
                                        "Register successful, you may now log in", Toast.LENGTH_LONG).show();
                                addNewUser(emailText.getEditableText().toString());
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "E-mail already in use or invalid", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signInText = (TextView) findViewById(R.id.signInText);

        emailText = (EditText) findViewById(R.id.textViewEmailRegister);
        passwordText = (EditText) findViewById(R.id.textTextTextPasswordRegister);
        passwordConfirmation = (EditText) findViewById(R.id.textTextTextPasswordConfirmation);

        registerButton = (AppCompatButton) findViewById(R.id.signupButton);



        mAuth = FirebaseAuth.getInstance();

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this,
                        "Registering...", Toast.LENGTH_LONG).show();
                register();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }


}