package com.example.projecttwodundivedantam;

public class GymClass {

    private long id;
    private String name;
    private String instructor;
    private String time;
    private String mat;

    public GymClass(long id, String name, String instructor, String time, String mat) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
        this.time = time;
        this.mat = mat;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getTime() {
        return time;
    }

    public String getMat() {
        return mat;
    }

    @Override
    public String toString() {
        return time + " - " + name + "\nInstructor: " + instructor + ", Mat: " + mat;
    }
}