package com.example.thomascomercs360project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput, rePasswordInput;
    Button registerButton, signinButton;
    LoginDatabase loginDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);
        rePasswordInput = (EditText) findViewById(R.id.re_enter_password);

        registerButton = (Button) findViewById(R.id.sign_up_button);
        signinButton = (Button) findViewById(R.id.sign_in_button);

        loginDatabase = new LoginDatabase(this);

        // Allows users to register with the provided username and password.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = usernameInput.getText().toString();
                String pass = passwordInput.getText().toString();
                String repass = rePasswordInput.getText().toString();

                // Checking for valid data.
                if (user.equals("") || (pass.equals("") || repass.equals("")))
                    Toast.makeText(RegisterActivity.this, "Please check all fields", Toast.LENGTH_SHORT).show();
                else {
                    // Ensures that both password fields match.
                    if (pass.equals(repass)) {
                        // Checks for the username in the database.
                        Boolean checkUser = loginDatabase.checkUsername(user);
                        if (checkUser == false) {
                            // Inserts the username and password into the database.
                            Boolean insert = loginDatabase.insertData(user, pass);
                            // Start the main activity for login.
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "User already exists you can sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Navigate to the login screen.
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
