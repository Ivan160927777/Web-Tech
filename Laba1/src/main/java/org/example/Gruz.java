package org.example;

public class Gruz {
    private int id;
    private String name;
    private int skladId;

    public Gruz() {}
    public Gruz(int id, String name, int skladId) {
        this.id = id;
        this.name = name;
        this.skladId = skladId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSkladId() { return skladId; }
    public void setSkladId(int skladId) { this.skladId = skladId; }

    @Override
    public String toString() {
        return "Gruz{id=" + id + ", name='" + name + "', skladId=" + skladId + "}";
    }
}
