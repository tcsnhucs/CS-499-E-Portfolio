package com.example.thomascomercs360project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button signButton, registerButton;

    LoginDatabase loginDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username_one);
        password = (EditText) findViewById(R.id.password_one);
        signButton = (Button) findViewById(R.id.sign_in_button_one);
        registerButton = (Button) findViewById(R.id.register_button);
        loginDatabase = new LoginDatabase(this);

        // Set up the log in button listener.
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString();
                String passWord = password.getText().toString();

                // Makes sure all required fields are filled.
                if (userName.isEmpty() || passWord.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Make sure you filled in all the fields", Toast.LENGTH_SHORT).show();
                }
                // This will look to see if the username and password already exist and to see if it is a match in the database.
                else {
                    Boolean checkPass = loginDatabase.checkUsernamePassword(userName, passWord);
                    if (checkPass == true) {
                        Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        // Redirect straight to CalendarActivity
                        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // This is for the user to be able to register and have their data stored in the database.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
