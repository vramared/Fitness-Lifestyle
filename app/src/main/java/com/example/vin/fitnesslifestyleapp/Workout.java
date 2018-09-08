package com.example.vin.fitnesslifestyleapp;

public class Workout {
    private String imgURL, name, date, sets, reps;

    public Workout(String imgURL, String name, String date, String sets, String reps) {
        this.imgURL = imgURL;
        this.name = name;
        this.date = date;
        this.sets = sets + " Sets";
        this.reps = reps + " Reps";
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getSets() {
        return sets;
    }

    public String getReps() {
        return reps;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }
}
