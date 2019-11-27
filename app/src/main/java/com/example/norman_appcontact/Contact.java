package com.example.norman_appcontact;

import java.io.Serializable;

public class  Contact implements Serializable {

    private String id, phone, name, email, picture;

    public Contact(){}
    public Contact(String Id, String Name, String Phone, String Email, String Picture){
        this.id = Id;
        this.name = Name;
        this.phone = Phone;
        this.email = Email;
        this.picture = Picture;
    }

    public void setPicture(String Picture){
        this.picture = Picture;
    }
    public String getPicture(){
        return this.picture;
    }
    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setId(String ID){
        this.id = ID;
    }

    public void setName(String Name){
        this.name = Name;
    }

    public void setEmail(String Email){
        this.email=Email;
    }

    public void setPhone(String Phone){
        this.phone = Phone;
    }
}
