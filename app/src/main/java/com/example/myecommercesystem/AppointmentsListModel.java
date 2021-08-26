package com.example.myecommercesystem;

public class AppointmentsListModel {


    public String name;
    public String date;
    public String location;
    public String item_push_id;
    public String value;


    public AppointmentsListModel(String name, String date, String location, String item_push_id, String value) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.item_push_id = item_push_id;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getItem_push_id() {
        return item_push_id;
    }

    public String getValue() {
        return value;
    }
}
