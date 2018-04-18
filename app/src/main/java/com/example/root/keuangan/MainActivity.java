package com.example.root.keuangan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Proses
        db = new DatabaseHelper(this);
        setStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatus();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cash) {
            Intent intent = new Intent("com.example.root.keuangan.Cash");
            startActivity(intent);
        } else if (id == R.id.nav_report) {
            Intent intent = new Intent("com.example.root.keuangan.Report");
            startActivity(intent);
        } else if (id == R.id.nav_statistic) {
            Toast.makeText(MainActivity.this, "Segera hadir di versi mendatang :)", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_help) {
            Toast.makeText(MainActivity.this, "Langsung tanya sama admin aja ya", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Main Menu
    public void changeIntent(String id){
        Intent intent = new Intent("com.example.root.keuangan.Spending");
        intent.putExtra("title", id);
        startActivity(intent);
    }

    public void kantor(View v)     { changeIntent("Kantor"); }
    public void hobi(View v)       { changeIntent("Hobi"); }
    public void rumahTangga(View v){ changeIntent("Rumah Tangga"); }
    public void makan(View v)      { changeIntent("Makan"); }
    public void kendaraan(View v)  { changeIntent("Kendaraan"); }
    public void lain(View v)       { changeIntent("Lain-lain"); }

    public void setStatus(){
        Integer wWeek;
        Expanse expanse = new Expanse();
        if (expanse.day <= 7) wWeek = 0;
        else if (expanse.day <= 14) wWeek = 1;
        else if (expanse.day <= 21) wWeek = 2;
        else wWeek = 3;
        expanse.getMaxDay();

        TextView weekly  = (TextView) findViewById(R.id.main_val_week);
        TextView monthly = (TextView) findViewById(R.id.main_val_month);
        TextView cash    = (TextView) findViewById(R.id.total_cash);
        weekly.setText("Rp. "  + db.getWeekly(wWeek + 1, expanse.year + "-" + expanse.monthZ + "-", expanse.maxDay, "Semua"));
        monthly.setText("Rp. " + db.getDaily(expanse.year + "-" + expanse.monthZ, "Semua"));
        cash.setText("Rp. " + db.getKas());
    }
}
