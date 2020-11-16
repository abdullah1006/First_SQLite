package com.example.firstsqlite.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final String database_name ="db_perpus";
    public static final String table_name = "table_perpus";

    public static final String row_id = "_id";
    public static final String row_nama = "Nama";
    public static final String row_judul = "Judul";
    public static final String row_pinjam = "TglPinjam";
    public static final String row_kembali = "TglKembali";
    public static final String row_status = "Status";

    private SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context, database_name, null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + table_name + "(" + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_nama + " TEXT," + row_judul + " TEXT," + row_pinjam + " TEXT," + row_kembali + " TEXT," + row_status + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
    }

    //Get All SQLite data
    public Cursor allData(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name + " ORDER BY " + row_id + " DESC ",null);
        return cur;
    }

    //Get 1 Data By ID
    public Cursor oneData(long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + row_id + "=" + id,null);
        return cur;
    }

    //Insert Data
    public void insertData(ContentValues values){
        db.insert(table_name,null,values);
    }

    //Update Data
    public void updateData(ContentValues values,long id){
        db.update(table_name,values, row_id + "=" + id,null);
    }

    //Delete Data
    public void deleteData(long id){
        db.delete(table_name, row_id + "=" + id,null);
    }
}
