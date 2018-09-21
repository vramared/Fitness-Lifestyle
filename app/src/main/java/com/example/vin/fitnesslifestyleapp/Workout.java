package com.example.vin.fitnesslifestyleapp;

public class Workout {
    private String name, date, sets, reps;
    private int imgURL;

    public Workout(int imgURL, String name, String date, String sets, String reps) {
        this.imgURL = imgURL;
        this.name = name;
        this.date = date;
        this.sets = sets + " Sets";
        this.reps = reps + " Reps";
    }

    public int getImgURL() {
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

    public void setImgURL(int imgURL) {
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

    public String toString() {
        return this.date + ": " + this.sets + " " + this.reps;
    }

    public boolean equals(Workout holder) {
        boolean equal = false;
        if(this.getName().equals(holder.getName()) && this.getDate().equals(holder.getDate()) &&
                this.getSets().equals(holder.getSets()) && this.getReps().equals(holder.getReps())) {
            equal = true;
        }
        return equal;
    }
}
