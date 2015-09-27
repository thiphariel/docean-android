package com.thiphariel.docean;

/**
 * Created by Thiphariel on 27/09/2015.
 */
public class DropletsViewItem {
    private String id;
    private String name;
    private int cpus;
    private int size;

    public DropletsViewItem(String id, String name, int cpus, int size) {
        this.id = id;
        this.name = name;
        this.cpus = cpus;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getCpus() {
        return cpus;
    }

    public int getSize() {
        return size;
    }
}

