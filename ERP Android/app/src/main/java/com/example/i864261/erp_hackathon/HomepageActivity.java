package com.example.i864261.erp_hackathon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class HomepageActivity extends AppCompatActivity {

    private String username;

    // views
    private TextView welcomeTextView, pastReportTextView;
    private Button createNewButtonView;
    private ImageButton logoutImageButtonView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        username = getIntent().getStringExtra("username");
        //capitalizing first character of the name
        username = Character.toString(username.charAt(0)).toUpperCase() + username.substring(1);
        welcomeTextView.setText("Welcome, " + username);
        pastReportTextView = findViewById(R.id.past_reports);
    }

    private void init(){
        createNewButtonView = findViewById(R.id.create_new);
        logoutImageButtonView = findViewById(R.id.logout);
        welcomeTextView = findViewById(R.id.welcome);
    }

    public void createNewReport(View view){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        startActivity(i);
    }

    public void logout(View view){

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(HomepageActivity.this, LoginActivity.class);
                        startActivity(i);
                    }})
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void runPastReports(View view){
        Intent i = new Intent(this, PastReports.class);
        i.putExtra("username", username);
        startActivity(i);
    }
}
