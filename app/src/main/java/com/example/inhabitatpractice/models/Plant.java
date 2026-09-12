package com.example.inhabitatpractice.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Plant {
    private final String name;
    private final String description;
    private final List<PlantImage> images;

    public Plant(String name, String description, List<PlantImage> images) {
        this.name = name;
        this.description = description;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PlantImage> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return name;
    }

    public static List<Plant> getPlants() {
        return new ArrayList<>(Arrays.asList(
                new Plant(
                        "Narra",
                        "A strong native hardwood tree and the national tree of the Philippines.",
                        defaultImages()),
                new Plant(
                        "Mango",
                        "A tropical fruit tree that grows best in warm, sunny locations.",
                        defaultImages()),
                new Plant(
                        "Molave",
                        "A durable native tree that tolerates dry conditions.",
                        defaultImages()),
                new Plant(
                        "Acacia",
                        "A fast-growing shade tree with a broad canopy.",
                        defaultImages()),
                new Plant(
                        "Banaba",
                        "A flowering native tree known for its purple blossoms.",
                        defaultImages())
        ));
    }

    private static List<PlantImage> defaultImages() {
        return Arrays.asList(
                new PlantImage(1, "default_1"),
                new PlantImage(5, "default_2"),
                new PlantImage(10, "default_3")
        );
    }

    public static class PlantImage {
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

}
