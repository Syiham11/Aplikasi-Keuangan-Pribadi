package com.example.root.keuangan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }

    public void callExpanse(String title){
        Intent intent = new Intent("com.example.root.keuangan.Expanse");
        intent.putExtra("title", title);
        intent.putExtra("cat", "Semua");
        startActivity(intent);
    }

    public void btnDay(View v) {
        callExpanse("Harian");
    }
    public void btnWeek(View v) {
        callExpanse("Mingguan");
    }
    public void btnMonth(View v) {
        callExpanse("Bulanan");
    }
    public void btnAll(View v) {
        callExpanse("All");
    }

}