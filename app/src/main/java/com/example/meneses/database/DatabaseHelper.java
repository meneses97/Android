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
        db.execSQL("INSERT INTO user (email, name, password) VALUES ('dinho@gmail.com','Aderito', '12345')");
        db.execSQL("INSERT INTO user (email, name, password) VALUES ('admin@gmail.com','Adelito', '00011')");

        db.execSQL(ScriptDDL.createRotaTable());
        db.execSQL("INSERT INTO rota (origem, destino) VALUES ('emulacao','muxara')");
        db.execSQL("INSERT INTO rota (origem, destino) VALUES ('baixa','gingone')");
        db.execSQL("INSERT INTO rota (origem, destino) VALUES ('cimento','wimbe')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists rota");
    }
}
