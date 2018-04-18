package com.example.root.keuangan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.NumberFormat;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String dbName = "keuangan.db";
    public static String table  = "history";
    private String dateStart, dateEnd;

    public DatabaseHelper(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + table + " (id INTEGER PRIMARY KEY AUTOINCREMENT, cat TEXT, desc TEXT, nominal INTEGER, tanggal TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + table);
        onCreate(db);
    }

    //CRUD Method
    public boolean insertKas(String desc, Integer nominal, String tanggal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put("cat", "Kas");
        cv.put("desc", desc);
        cv.put("nominal", nominal);
        cv.put("tanggal", tanggal);

        long result = db.insert(table, null, cv);
        if(result == -1) return false;
        else return true;
    }

    public String getKas(){
        Integer totalCash = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT nominal FROM " + table + " WHERE cat = 'Kas'", null);

        while (result.moveToNext()) {
            totalCash += Integer.parseInt(result.getString(0));
        }
        return NumberFormat.getInstance().format(totalCash - getExpanse("Semua"));
    }

    public String getDaily(String tanggal, String cat){
        Integer totalExpanse = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT nominal FROM " + table + " WHERE cat = '"+cat+"' AND tanggal LIKE '"+tanggal+"%'", null);
        if(cat.equals("Semua")) result = db.rawQuery("SELECT nominal FROM " + table + " WHERE cat != 'Kas' AND tanggal LIKE '"+tanggal+"%'", null);

        while (result.moveToNext()) {
            totalExpanse += Integer.parseInt(result.getString(0));
        }
        return NumberFormat.getInstance().format(totalExpanse);
    }

    public String getWeekly(Integer week, String monthYear, Integer maxDay, String cat){
        String start = "01";
        String end   = "08";
        if(week.equals(2)){
            start = "08";
            end   = "15";
        } else if(week.equals(3)){
            start = "15";
            end   = "22";
        } else if (week.equals(4)) {
            start = "22";
            end   = maxDay.toString() + 1;
        }

        dateStart = monthYear + start;
        dateEnd   = monthYear + end;

        Integer totalExpanse = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT nominal FROM " + table + " WHERE cat = '"+cat+"' AND tanggal BETWEEN '"+dateStart+"' AND '"+dateEnd+"'", null);
        if(cat.equals("Semua")) result     = db.rawQuery("SELECT nominal FROM " + table + " WHERE cat != 'Kas' AND tanggal BETWEEN '"+dateStart+"' AND '"+dateEnd+"'", null);

        while (result.moveToNext()) {
            totalExpanse += Integer.parseInt(result.getString(0));
        }
        return NumberFormat.getInstance().format(totalExpanse);
    }

    public Integer getExpanse(String cat){
        Integer totalExpanse = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT nominal FROM " + table + " WHERE cat = '"+cat+"'", null);
        if(cat.equals("Semua")) result = db.rawQuery("SELECT nominal FROM " + table + " WHERE cat != 'Kas'", null);
        while (result.moveToNext()) { totalExpanse += Integer.parseInt(result.getString(0)); };
        return totalExpanse;
    }

    public Cursor selectKas(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT * FROM " + table + " WHERE cat = 'Kas' ORDER BY tanggal DESC", null);
        return result;
    }

    public Cursor selectDaily(String tanggal, String cat, boolean search, String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT * FROM " + table + " WHERE cat = '"+cat+"' AND tanggal LIKE '"+tanggal+"%' ORDER BY tanggal DESC", null);
        if(cat.equals("Semua")) result = db.rawQuery("SELECT * FROM " + table + " WHERE cat != 'Kas' AND tanggal LIKE '"+tanggal+"%' ORDER BY tanggal DESC", null);
        if(search){
            result = db.rawQuery("SELECT * FROM " + table + " WHERE desc LIKE '%"+query+"%' AND cat = '"+cat+"' AND tanggal LIKE '"+tanggal+"%' ORDER BY tanggal DESC", null);
            if(cat.equals("Semua")) result = db.rawQuery("SELECT * FROM " + table + " WHERE desc LIKE '%"+query+"%' AND cat != 'Kas' AND tanggal LIKE '"+tanggal+"%' ORDER BY tanggal DESC", null);
        }
        return result;
    }

    public Cursor selectWeekly(Integer week, String monthYear, Integer maxDay, String cat, boolean search, String query) {
        String start = "01";
        String end   = "08";
        if (week.equals(2)) {
            start = "08";
            end = "15";
        } else if (week.equals(3)) {
            start = "15";
            end = "22";
        } else if (week.equals(4)) {
            start = "22";
            end = maxDay.toString() + 1;
        }

        dateStart = monthYear + start;
        dateEnd = monthYear + end;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + table + " WHERE cat = '"+cat+"' AND tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "' ORDER BY tanggal DESC", null);
        if(cat.equals("Semua")) result = db.rawQuery("SELECT * FROM " + table + " WHERE cat != 'Kas' AND tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "' ORDER BY tanggal DESC", null);
        if(search){
            result = db.rawQuery("SELECT * FROM " + table + " WHERE desc LIKE '%"+query+"%' AND cat = '"+cat+"' AND tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "' ORDER BY tanggal DESC", null);
            if(cat.equals("Semua")) result = db.rawQuery("SELECT * FROM " + table + " WHERE desc LIKE '%"+query+"%' AND cat != 'Kas' AND tanggal BETWEEN '" + dateStart + "' AND '" + dateEnd + "' ORDER BY tanggal DESC", null);
        }
        return result;
    }

    public Cursor selectAll(String cat, boolean search, String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT * FROM " + table + " WHERE cat = '"+cat+"' ORDER BY tanggal DESC", null);
        if(cat.equals("Semua")) result = db.rawQuery("SELECT * FROM " + table + " WHERE cat != 'Kas' ORDER BY tanggal DESC", null);
        if(search){
            result = db.rawQuery("SELECT * FROM " + table + " WHERE desc LIKE '%"+query+"%' AND cat = '"+cat+"' ORDER BY tanggal DESC", null);
            if(cat.equals("Semua")) result = db.rawQuery("SELECT * FROM " + table + " WHERE desc LIKE '%"+query+"%' AND cat != 'Kas' ORDER BY tanggal DESC", null);
        }
        return result;
    }

    public Cursor selectItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result     = db.rawQuery("SELECT * FROM " + table + " WHERE id = '"+id+"'", null);
        return result;
    }

    public boolean deleteItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(table, "id = ?", new String[] {id} );
        if(result == -1) return false;
        else return true;
    }

    public boolean insertSpending(String desc, Integer nominal, String tanggal, String isType){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put("cat", isType);
        cv.put("desc", desc);
        cv.put("nominal", nominal);
        cv.put("tanggal", tanggal);

        long result = db.insert(table, null, cv);
        if(result == -1) return false;
        else return true;
    }

    public boolean updateItem(String id, String desc, Integer nominal, String tanggal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  cv = new ContentValues();
        cv.put("desc", desc);
        cv.put("nominal", nominal);
        cv.put("tanggal", tanggal);

        long result = db.update(table, cv, "id = ?", new String[] {id} );
        if(result == -1) return false;
        else return true;
    }
}