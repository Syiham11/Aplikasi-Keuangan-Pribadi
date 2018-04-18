package com.example.root.keuangan;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@SuppressWarnings("ALL")
public class Expanse extends AppCompatActivity {
    public String isType, isCat, query, dateNow, dateShow, nominal, dayZ, monthZ, sDayValue, sMonthValue;
    public Integer maxDay, wWeek, expanse;
    private boolean search = false;
    private String[] arraySpinner;
    private Spinner sDay, sMonth, sYear, sCat;
    DatabaseHelper db;

    Calendar date  = new GregorianCalendar();
    Integer year   = date.get(Calendar.YEAR);
    Integer month  = date.get(Calendar.MONTH) + 1;
    Integer day    = date.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanse);
        db = new DatabaseHelper(this);
        getMaxDay();

        sDay   = (Spinner) findViewById(R.id.spinnerDay);
        sMonth = (Spinner) findViewById(R.id.spinnerMonth);
        sYear  = (Spinner) findViewById(R.id.spinnerYear);
        sCat   = (Spinner) findViewById(R.id.spinnerCat);

        isCat  = getIntent().getStringExtra("cat");
        isType = getIntent().getStringExtra("title");
        comboCat();
        if (!isType.equals("All") && !isType.equals("Kas")) {
            comboDay();
            comboMonth();
            comboYear();
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("Pengeluaran " + isType);
            setVal();
        } else {
            sDay.setVisibility(View.GONE);
            sMonth.setVisibility(View.GONE);
            sYear.setVisibility(View.GONE);

            if(isType.equals("All")) {
                if (getSupportActionBar() != null) getSupportActionBar().setTitle("Semua Pengeluaran");
            } else {
                sCat.setVisibility(View.GONE);
                if (getSupportActionBar() != null) getSupportActionBar().setTitle("Kas");
                TextView typeExpanse = (TextView)findViewById(R.id.expanse_total);
                typeExpanse.setText("Total Kas :");
                getCashHistory();
            }
        }

        itemListShow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemListShow();
    }

    public void getMaxDay(){
        if (month == 2) {
            Calendar cal = new GregorianCalendar(year, Calendar.FEBRUARY, 1);
            maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        } else if (month == 4 || month == 6 || month == 9 || month == 11) maxDay = 30;
        else maxDay = 31;

        dayZ        = day.toString();
        monthZ      = month.toString();
        if(month < 10) monthZ = "0" + month;
        if(day   < 10)   dayZ = "0" + day;
        dateNow = year + "-" + monthZ + "-" + dayZ;
    }

    // ComboBox
    public void comboDay() {
        //Spinner
        switch (isType) {
            case "Harian":
                arraySpinner = new String[maxDay];
                Integer i = 1;
                while (i <= maxDay) {
                    arraySpinner[i - 1] = i.toString();
                    i++;
                }
                break;
            case "Mingguan":
                arraySpinner = new String[4];
                arraySpinner[0] = "1";
                arraySpinner[1] = "2";
                arraySpinner[2] = "3";
                arraySpinner[3] = "4";
                break;
            default:
                sDay.setVisibility(View.GONE);
                break;
        }

        if (isType.equals("Harian") || isType.equals("Mingguan")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner, arraySpinner);
            sDay.setAdapter(adapter);

            switch (isType) {
                case "Harian":
                    sDay.setSelection(day - 1);
                    break;
                default:
                    if(day <= 7) wWeek = 0;
                    else if (day <= 14) wWeek = 1;
                    else if (day <= 21) wWeek = 2;
                    else wWeek = 3;
                    sDay.setSelection(wWeek);
                    break;
            }

            sDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    search = false;
                    if(isType.equals("Harian")) changeHarian();
                    else changeMingguan();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void comboMonth() {
        arraySpinner = new String[12];
        Integer i = 1;
        while (i <= 12) {
            arraySpinner[i - 1] = i.toString();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner, arraySpinner);
        sMonth.setAdapter(adapter);
        sMonth.setSelection(month - 1);

        sMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search = false;
                year   = Integer.parseInt(sYear.getSelectedItem().toString());
                if(isType.equals("Harian")){
                    changeHarian();
                    month = Integer.parseInt(sMonthValue);
                    day   = Integer.parseInt(sDayValue);
                    getMaxDay();
                    comboDay();
                } else if (isType.equals("Mingguan")) changeMingguan();
                else if(isType.equals("Bulanan")) changeBulanan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void comboYear() {
        Integer yearDB    = 2016;
        Integer totalYear = year - yearDB;
        arraySpinner = new String[totalYear + 1];
        Integer i = 0;
        while (i <= totalYear) {
            Integer yearShow = yearDB + i;
            arraySpinner[i]  = yearShow.toString();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner, arraySpinner);
        sYear.setAdapter(adapter);
        sYear.setSelection(totalYear);

        sYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search = false;
                if(isType.equals("Harian")){
                    changeHarian();
                    year  = Integer.parseInt(sYear.getSelectedItem().toString());
                    month = Integer.parseInt(sMonthValue);
                    day   = Integer.parseInt(sDayValue);
                    getMaxDay();
                    comboDay();
                }  else if (isType.equals("Mingguan")) changeMingguan();
                else if(isType.equals("Bulanan")) changeBulanan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void comboCat() {
        arraySpinner = new String[7];
        arraySpinner[0] = "Kantor";
        arraySpinner[1] = "Makan";
        arraySpinner[2] = "Hobi";
        arraySpinner[3] = "Kendaraan";
        arraySpinner[4] = "Rumah Tangga";
        arraySpinner[5] = "Lain-lain";
        arraySpinner[6] = "Semua";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner, arraySpinner);
        sCat.setAdapter(adapter);

        switch (isCat){
            case "Kantor"      : sCat.setSelection(0); break;
            case "Makan"       : sCat.setSelection(1); break;
            case "Hobi"        : sCat.setSelection(2); break;
            case "Kendaraan"   : sCat.setSelection(3); break;
            case "Rumah Tangga": sCat.setSelection(4); break;
            case "Lain-lain"   : sCat.setSelection(5); break;
            default: sCat.setSelection(6); break;
        }

        sCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search = false;
                if(isType.equals("Harian")){
                    changeHarian();
                    year  = Integer.parseInt(sYear.getSelectedItem().toString());
                    month = Integer.parseInt(sMonthValue);
                    day   = Integer.parseInt(sDayValue);
                    getMaxDay();
                    comboDay();
                }  else if (isType.equals("Mingguan")) changeMingguan();
                else if(isType.equals("Bulanan")) changeBulanan();
                else changeBulanan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void changeHarian(){
        isCat       = sCat.getSelectedItem().toString();
        sDayValue   = sDay.getSelectedItem().toString();
        sMonthValue = sMonth.getSelectedItem().toString();
        if(Integer.parseInt(sMonthValue) < 10) sMonthValue = "0" + sMonthValue;
        if(Integer.parseInt(sDayValue)   < 10) sDayValue   = "0" + sDayValue;

        dateNow = sYear.getSelectedItem().toString() + "-" + sMonthValue + "-" + sDayValue;
        itemListShow();
        setVal();
    }

    public void changeMingguan(){
        isCat       = sCat.getSelectedItem().toString();
        wWeek       = Integer.parseInt(sDay.getSelectedItem().toString()) - 1;
        sMonthValue = sMonth.getSelectedItem().toString();
        year        = Integer.parseInt(sYear.getSelectedItem().toString());
        if(Integer.parseInt(sMonthValue) < 10) monthZ = "0" + sMonthValue;

        month = Integer.parseInt(sMonthValue);
        getMaxDay();
        itemListShow();
        setVal();
    }

    public void changeBulanan(){
        isCat       = sCat.getSelectedItem().toString();
        if(!isType.equals("All")){
            sMonthValue = sMonth.getSelectedItem().toString();
            year        = Integer.parseInt(sYear.getSelectedItem().toString());
            if(Integer.parseInt(sMonthValue) < 10) monthZ = "0" + sMonthValue;
        }

        itemListShow();
        setVal();
    }

    //ListView
    public void itemListShow() {
        ListView modelList = (ListView) findViewById(R.id.listView);
        List<ModelList> listModel = new ArrayList<>();

        Cursor result = db.selectAll(isCat, search, query);
        if(isType.equals("Kas"))           result = db.selectKas();
        else if(isType.equals("Harian"))   result = db.selectDaily(dateNow, isCat, search, query);
        else if(isType.equals("Mingguan")) result = db.selectWeekly(wWeek + 1, year + "-" + monthZ + "-", maxDay, isCat, search, query);
        else if(isType.equals("Bulanan"))  result = db.selectDaily(year + "-" + monthZ, isCat, search, query);

        if(result.getCount() != 0) {
            Integer i = 1;
            expanse   = 0;
            while (result.moveToNext()) {
                dateShow  = result.getString(4);
                listModel.add(new ModelList(i, Integer.parseInt(result.getString(0)), result.getString(2), "Rp. " + NumberFormat.getInstance().format(Integer.parseInt(result.getString(3))), dateShow, isType));
                i++;

                if(search){
                    expanse += Integer.parseInt(result.getString(3));
                    nominal  = NumberFormat.getInstance().format(expanse);
                }
            }
        }

        ListAdapter adapter = new ListAdapter(getApplicationContext(), listModel);
        modelList.setAdapter(adapter);

        modelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("com.example.root.keuangan.Detail");
                intent.putExtra("id", view.getTag().toString());
                intent.putExtra("type", isType);
                intent.putExtra("cat", isCat);
                startActivity(intent);
            }
        });
    }

    //SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search = true;
                query  = searchView.getQuery().toString();
                if(isType.equals("Harian")) changeHarian();
                else if(isType.equals("Mingguan")) changeBulanan();
                else changeBulanan();
                setVal();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //Data Proses
    public void getCashHistory(){
        TextView moneyAll = (TextView)findViewById(R.id.money_value);
        String totalCash = "Rp. " + db.getKas();
        moneyAll.setTextColor(Color.parseColor("#55AA33"));
        moneyAll.setText(totalCash);
    }

    public void setVal(){
        TextView moneyAll = (TextView)findViewById(R.id.money_value);
        if(!search) {
            if      (isType.equals("Harian"))   nominal = db.getDaily(dateNow, isCat);
            else if (isType.equals("Mingguan")) nominal = db.getWeekly(wWeek + 1, year + "-" + monthZ + "-", maxDay, isCat);
            else if (isType.equals("Bulanan"))  nominal = db.getDaily(year + "-" + monthZ, isCat);
            else nominal = NumberFormat.getInstance().format(db.getExpanse(isCat));
        }
        String totalExpanse = "Rp. " + nominal;
        moneyAll.setText(totalExpanse);
    }
}
