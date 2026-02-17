package com.example.thomascomercs360project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int smsPermissionCode = 1;
    EditText username, password;
    Button signButton, registerButton, requestButton;

    LoginDatabase loginDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username_one);
        password = (EditText) findViewById(R.id.password_one);
        signButton = (Button) findViewById(R.id.sign_in_button_one);
        registerButton = (Button) findViewById(R.id.register_button);
        requestButton = (Button) findViewById(R.id.request_button);
        loginDatabase = new LoginDatabase(this);

        createNotificationChannel();    // Create a channel for notifications.

        // Set up the log in button listener.
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString();
                String passWord = password.getText().toString();

                // Makes sure all required fields are filled.
                if (userName.equals("") || passWord.equals("")) {
                    Toast.makeText(MainActivity.this, "Make sure you filled in all the fields", Toast.LENGTH_SHORT).show();
                }
                // This will look to see if the username and password already exist and to see if it is a match in the database.
                else {
                    Boolean checkPass = loginDatabase.checkUsernamePassword(userName, passWord);
                    if (checkPass == true) {
                        Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);

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

    private void createNotificationChannel() {
        CharSequence name = "Reminder";
        String desc = "For Reminder";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notifyText", name, importance);
        channel.setDescription(desc);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
