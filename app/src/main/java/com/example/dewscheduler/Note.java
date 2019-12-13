package com.example.dewscheduler;

public class Note {
    private String title;
    private String description;
    private int number;
    private int icon;

    public Note (){
        //empty but needed
    }

    public Note (String title, String description, int number, int icon){
        this.title = title;
        this.description = description;
        this.number = number;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getNumber() {
        return number;
    }

    public int getIcon() {
        return icon;
    }
}
