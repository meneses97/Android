package com.example.meneses.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.meneses.database.DatabaseHelper;
import com.example.meneses.entities.Rota;

import java.util.ArrayList;
import java.util.List;

public class RotaController {
    private DatabaseHelper databaseHelper;
    private StringBuilder sql;
    private SQLiteDatabase connection;

    public RotaController(SQLiteDatabase connection) {
        this.connection = connection;
    }

    public void insert(String o, String d){
        ContentValues contentValues = new ContentValues();

        contentValues.put("origem", o);
        contentValues.put("destino", d);

        connection.insertOrThrow("rota",null, contentValues);
    }

    public void remove(Rota rota){
        String[] parameters = new String[2];
        parameters[0] = rota.getOrigem();
        parameters[1] = rota.getDestino();

        connection.delete("rota","origem = ? AND destino = ?", parameters);
    }

    public void edit(int id, String o, String d){
        ContentValues contentValues = new ContentValues();

        contentValues.put("origem", o);
        contentValues.put("destino", d);

        String[] parameters = new String[1];
        parameters[0] = String.valueOf(id);

        connection.update("rota", contentValues, "idrota = ?", parameters);
    }

    public List<Rota> fetchAll(){
        List<Rota> rotaList = new ArrayList<>();

        sql = new StringBuilder();
        sql.append("SELECT idrota,");
        sql.append("       origem,");
        sql.append("       destino");
        sql.append("  FROM rota;");

        try {
            Cursor result = connection.rawQuery(sql.toString(), null);

            if(result.getCount() > 0){
                result.moveToFirst();

                do{
                    Rota rota = new Rota();

                    rota.setIdrota(result.getInt(result.getColumnIndexOrThrow("idrota")));
                    rota.setOrigem(result.getString(result.getColumnIndexOrThrow("origem")));
                    rota.setDestino(result.getString(result.getColumnIndexOrThrow("destino")));
                    
                    rotaList.add(rota);
                }while(result.moveToNext());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return rotaList;
    }

    public Rota fetchOne(String origin, String dest){
        Rota rota = new Rota();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT idrota,");
        sql.append("       origem,");
        sql.append("       destino");
        sql.append("  FROM rota");
        sql.append("  WHERE origem = ? AND destino = ?");

        String[] parameters = new String[2];
        parameters[0] = origin;
        parameters[1] = dest;

        Cursor result = connection.rawQuery(sql.toString(), parameters);

        if(result.getCount() > 0){
            result.moveToFirst();

            rota.setIdrota(result.getInt(result.getColumnIndexOrThrow("idrota")));
            rota.setOrigem(result.getString(result.getColumnIndexOrThrow("origem")));
            rota.setDestino(result.getString(result.getColumnIndexOrThrow("destino")));

            return rota;
        }

        return null;
    }
}
