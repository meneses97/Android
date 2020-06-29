package com.example.meneses.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.User;

public class UserController {
    SQLiteDatabase db;

    public UserController(SQLiteDatabase connection) {
        this.db = connection;
    }

    public boolean insertUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",user.getEmail());
        contentValues.put("name",user.getName());
        contentValues.put("password",user.getPassword());


        long ins = db.insert("user",null,contentValues);

        if (ins==-1) return false;

        else return true;
    }

    public User fetchOne(String email){
        User user = new User();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT email,");
        sql.append("       name,");
        sql.append("       password");
        sql.append("  FROM user");
        sql.append("  WHERE email = ?;");

        String[] parameters = new String[1];
        parameters[0] = email;

        Cursor result = db.rawQuery(sql.toString(), parameters);

        if(result.getCount() > 0){
            result.moveToFirst();

            user.setEmail(result.getString(result.getColumnIndexOrThrow("email")));
            user.setName(result.getString(result.getColumnIndexOrThrow("name")));
            user.setPassword(result.getString(result.getColumnIndexOrThrow("password")));

            return user;
        }
        return null;
    }

    public boolean checkemail(String email){
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE email=?", new String[]{email});

        if (cursor.getCount()>0) return false;
        return true;
    }

    public boolean checkAccount(String email, String passw){
        Cursor cursor  = db.rawQuery("SELECT * FROM user WHERE email=? AND password=?", new String[]{email,passw});

        return  (cursor.getCount()>0);
    }

}
