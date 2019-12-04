package com.example.dewscheduler;

public class Note {
    private String title;
    private String description;
    private int number;

    public Note (){
        //empty but needed
    }

    public Note (String title, String description, int number){
        this.title = title;
        this.description = description;
        this.number = number;

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
}
