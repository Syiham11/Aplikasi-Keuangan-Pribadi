package com.example.root.keuangan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class Detail extends AppCompatActivity {
    String idItem, isType, isCat;
    TextView title;
    EditText nominal, desc, date;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Detail");
        idItem = getIntent().getStringExtra("id");
        isType = getIntent().getStringExtra("type");
        isCat  = getIntent().getStringExtra("cat");
        db = new DatabaseHelper(this);

        title   = (TextView)findViewById(R.id.title_detail);
        nominal = (EditText)findViewById(R.id.detail_money);
        desc    = (EditText)findViewById(R.id.detail_desc);
        date    = (EditText)findViewById(R.id.detail_date);
        setValDetail();
    }

    public void setValDetail(){
        Cursor result    = db.selectItem(idItem);
        if(result.getCount() != 0) {
            while (result.moveToNext()) {
                title.setText(result.getString(1));
                desc.setText(result.getString(2));
                nominal.setText(result.getString(3));
                date.setText(result.getString(4));
            }
        }
    }

    public void editItem(View v){
        boolean res = db.updateItem(idItem, desc.getText().toString(), Integer.parseInt(nominal.getText().toString()), date.getText().toString());
        if(res) Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
    }

    public void deleteItem(View v){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Detail.this);
        alertBuilder.setMessage("Exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean res = db.deleteItem(idItem);
                        if(res){
                            Toast.makeText(getApplicationContext(), "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("com.example.root.keuangan.Expanse");
                            intent.putExtra("title", isType);
                            intent.putExtra("cat", isCat);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Delete Confirm");
        alert.show();
    }
}
