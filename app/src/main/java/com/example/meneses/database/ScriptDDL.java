package com.example.meneses.database;

public class ScriptDDL {

    public static String createUserTable(){
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE user(");
        builder.append(" email TEXT PRIMARY KEY,");
        builder.append(" name TEXT,");
        builder.append(" password TEXT);");

        return builder.toString();
    }

    public static String createRotaTable(){
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE rota(");
        builder.append(" idrota INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        builder.append(" origem TEXT NOT NULL,");
        builder.append(" destino TEXT NOT NULL);");

        return builder.toString();
    }
}
