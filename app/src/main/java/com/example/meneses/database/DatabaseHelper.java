package com.example.meneses.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper( Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ScriptDDL.createUserTable());
        db.execSQL("INSERT INTO user (email, name, password) VALUES ('d','Aderito', '12')");
        db.execSQL("INSERT INTO user (email, name, password) VALUES ('admin@gmail.com','Josue', '00011')");

        db.execSQL(ScriptDDL.createRotaTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists user");
//        db.execSQL("drop table if exists rota");
    }
}
