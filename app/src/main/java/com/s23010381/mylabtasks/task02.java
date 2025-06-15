package com.s23010381.mylabtasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class task02 extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextInputEditText usernameInput, passwordInput;
    private MaterialButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task02);


        dbHelper = new DatabaseHelper(this);


        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.button2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserDataAndNavigate();
            }
        });
    }

    private void saveUserDataAndNavigate() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();


        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        boolean isInserted = dbHelper.insertUser(username, password);

        if (isInserted) {
            Toast.makeText(this, "User data saved successfully!", Toast.LENGTH_SHORT).show();
            clearFields();


            Intent intent = new Intent(task02.this, task03.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        usernameInput.setText("");
        passwordInput.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}