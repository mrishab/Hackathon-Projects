package com.example.i864261.erp_hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final Map<String, String> credentials;

    static {
        credentials = new HashMap<String, String>();
        credentials.put("yuvi", "sharma");
        credentials.put("michael", "andersen");
        credentials.put("steve", "cho");
        credentials.put("jean-paul", "morneau");
        credentials.put("rishab", "manocha");
    }

    // views
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_login);
        init();
    }

    private void init(){
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
    }

    public void login(View view) {
        String username = usernameEditText.getText().toString().toLowerCase();
        String password = passwordEditText.getText().toString().toLowerCase();
        if (credentials.containsKey(username) && password.equals(credentials.get(username))){
            usernameEditText.setText("");
            passwordEditText.setText("");
            startHomePageActivity(username);
        } else {
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void startHomePageActivity(String username){
        Intent i = new Intent(this, HomepageActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }
}
