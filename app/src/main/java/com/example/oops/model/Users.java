package com.example.oops.model;

public class Users {

    private String name,password,phone,type;
    public Users()
    {

    }

    public Users(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
