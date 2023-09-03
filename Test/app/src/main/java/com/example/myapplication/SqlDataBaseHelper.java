package com.example.myapplication;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqlDataBaseHelper extends SQLiteOpenHelper {

    private static final String DataBaseName = "DataBaseIt";
    private static final int DataBaseVersion = 1;
    private static String DataBaseTable = "Anime_location";

    private SqlDataBaseHelper sqlDataBaseHelper;
    public SqlDataBaseHelper(@Nullable Context context) {
        super(context, DataBaseName, null, DataBaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SqlTable = "CREATE TABLE IF NOT EXISTS Anime_location (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name text not null," +
                "ep text not null," +
                "path text not null,"+
                "status text not null"+
                ")";
        sqLiteDatabase.execSQL(SqlTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL = "DROP TABLE Users";
        sqLiteDatabase.execSQL(SQL);
    }
    public void insertData(String name,String ep,String path,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("ep",ep);
        values.put("path",path);
        values.put("status",status);
        db.insert(DataBaseTable, null, values);
        db.close();
    }
    public void update_status(String name,String ep,String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status",status);
        db.update(DataBaseTable,values,"`name`='"+name+"' and `ep`='"+ep+"'",null);
    }
    public void del(String name,String ep){



        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(DataBaseTable, "`name`='"+name+"' and `ep`='"+ep+"'", null);

    }
    public ArrayList<Save_Information> All_data(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Save_Information> arrayList=new ArrayList<>();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + DataBaseTable, null);
        while (cursor.moveToNext()){
            String id=cursor.getString(0);
            String title=cursor.getString(1);
            String ep=cursor.getString(2);
            String path=cursor.getString(3);
            String status=cursor.getString(4);
            arrayList.add(new Save_Information(title,ep,path,status));
        }
        return  arrayList;
    }
}
