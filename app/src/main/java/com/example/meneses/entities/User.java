package com.example.meneses.entities;

import androidx.annotation.NonNull;

public class User {
    String email, name, password;
    Rota rota;

    public User(String email, String name, String password, Rota rota) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.rota = rota;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(){}

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String email){
        email = this.email;
    }

    public void setName(String name){
        name = this.name;
    }

    public void setPassword(String password){
        password = this.password;
    }

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

}
