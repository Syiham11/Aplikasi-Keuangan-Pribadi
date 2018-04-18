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

public class Cash extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        //Set Date
        String currentTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        EditText ed_tanggal = (EditText)findViewById(R.id.cash_date);
        ed_tanggal.setText(currentTimeString);

        db = new DatabaseHelper(this);
        setKas();
    }

    public void historyCash(View v){
        Intent intent = new Intent("com.example.root.keuangan.Expanse");
        intent.putExtra("title", "Kas");
        intent.putExtra("cat", "Kas");
        startActivity(intent);
    }

    public void insertDB(View v){
        EditText desc    = (EditText)findViewById(R.id.cash_desc);
        EditText nominal = (EditText)findViewById(R.id.cash_money);
        EditText tanggal = (EditText)findViewById(R.id.cash_date);
        if(nominal.getText().toString().equalsIgnoreCase("") || desc.getText().toString().equalsIgnoreCase("")) Toast.makeText(getApplicationContext(), "Mohon Lengkapi", Toast.LENGTH_SHORT).show();
        else {
            boolean inserted = db.insertKas(desc.getText().toString(), Integer.parseInt(nominal.getText().toString()), tanggal.getText().toString());
            if (!inserted) Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
        }

        //Hide Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nominal.getApplicationWindowToken(), 0);

        setKas();
    }

    public void setKas(){
        TextView cash    = (TextView) findViewById(R.id.value_cash);
        String totalCash = "Rp. " + db.getKas();
        cash.setText(totalCash);
    }
}