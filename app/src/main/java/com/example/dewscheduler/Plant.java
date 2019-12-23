package com.example.dewscheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Plant {
    private String title;
    private String description;
    private int number;
    private int icon;
    private LocalDateTime plantDate;
    private ArrayList<LocalDateTime> wateringDates = new ArrayList<>();

    public Plant() {
        //empty but needed
    }

    public Plant(String title, String description, int number, int icon, LocalDateTime plantDate) {
        this.title = title;
        this.description = description;
        this.number = number;
        this.icon = icon;
        this.plantDate = plantDate;
        this.wateringDates.add(plantDate.plusDays(number));
    }


    public LocalDateTime getWateringDate() {
        if (wateringDates.size() > 0)
            return wateringDates.get(wateringDates.size() - 1);
        else
            return LocalDateTime.now().plusDays(number);
    }

    public int getWateringLevel() {
        LocalDateTime last = getWateringDate();
        LocalDateTime cur = LocalDateTime.now();
        long days = Duration.between(cur, last).toDays();
        return (int) ((float)days / number) * 5;
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
