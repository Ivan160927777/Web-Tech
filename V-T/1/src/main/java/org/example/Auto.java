package org.example;

public class Auto {
    private int id;
    private String model;
    private String gosNumber;

    public Auto() {}

    public Auto(int id, String model, String gosNumber) {
        this.id = id;
        this.model = model;
        this.gosNumber = gosNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getGosNumber() { return gosNumber; }
    public void setGosNumber(String gosNumber) { this.gosNumber = gosNumber; }

    @Override
    public String toString() {
        return "Auto{id=" + id + ", model='" + model + "', gosNumber='" + gosNumber + "'}";
    }
}
