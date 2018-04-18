package com.example.root.keuangan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Spending extends AppCompatActivity {
    DatabaseHelper db;
    String isType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);
        db     = new DatabaseHelper(this);
        isType = getIntent().getStringExtra("title");

        //Set Title
        TextView tv_title = (TextView) findViewById(R.id.title_spend);
        EditText ed_tanggal = (EditText) findViewById(R.id.spend_date);
        tv_title.setText(isType);

        //Set Date
        String currentTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        ed_tanggal.setText(currentTimeString);
    }

    public void historyWeek(View v){
        Intent intent = new Intent("com.example.root.keuangan.Expanse");
        intent.putExtra("title", "Mingguan");
        intent.putExtra("cat", isType);
        startActivity(intent);
    }

    public void insertSpending(View v){
        EditText desc    = (EditText)findViewById(R.id.spend_desc);
        EditText nominal = (EditText)findViewById(R.id.spend_money);
        EditText tanggal = (EditText)findViewById(R.id.spend_date);
        if(nominal.getText().toString().equalsIgnoreCase("") || desc.getText().toString().equalsIgnoreCase("")) Toast.makeText(getApplicationContext(), "Mohon Lengkapi", Toast.LENGTH_SHORT).show();
        else {
            boolean inserted = db.insertSpending(desc.getText().toString(), Integer.parseInt(nominal.getText().toString()), tanggal.getText().toString(), isType);
            if (!inserted) Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
        }

        //Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nominal.getApplicationWindowToken(), 0);
    }
}
