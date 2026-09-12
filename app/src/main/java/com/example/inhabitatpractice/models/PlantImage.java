package com.example.inhabitatpractice.models;

public  class PlantImage {
    private final int age;
    private final String imageName;

    public PlantImage(int age, String imageName) {
        this.age = age;
        this.imageName = imageName;
    }

    public int getAge() {
        return age;
    }

    public String getImageName() {
        return imageName;
    }
}