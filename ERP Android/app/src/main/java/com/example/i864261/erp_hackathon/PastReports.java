package com.example.i864261.erp_hackathon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastReports extends AppCompatActivity {

    public static Map<String, List<String>> reports;
    public static List<String> users;

    static {
        initMap();
    }

    private String username;

    //Views
    private RecyclerView reportRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pastreports);
        init();
    }

    private void init(){
        String[] reportsAsArr = getListAsArr();
        reportRecyclerView = findViewById(R.id.list);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportRecyclerView.setAdapter(new ReportAdapter(this, reportsAsArr));
    }

    private String[] getListAsArr(){
        username = getIntent().getStringExtra("username");
        List<String> reportsForTheUser = reports.get(username);
        String[] reportsArr = new String[100];
        reportsForTheUser.toArray(reportsArr);
        return reportsArr;
    }

    private static void initMap(){
        users = new ArrayList<>();
        users.add("Yuvi");
        users.add("Rishab");
        users.add("Steve");
        users.add("Michael");
        users.add("Jean-paul");
        reports = new HashMap<>();

        for (String user: users){
            reports.put(user, new ArrayList<String>());
        }
    }
}
