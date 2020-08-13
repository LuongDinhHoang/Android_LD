package com.example.recyclerview.model;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private String className;
    private String image;

    public Student() {
    }

    public Student(String name, String className, String image) {
        this.name = name;
        this.className = className;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
