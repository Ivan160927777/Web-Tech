package org.example;
import java.sql.Date;

public class Perevoz {
    private int id;
    private int autoId;
    private int gruzId;
    private int skladId;
    private int driverId;
    private Date date;

    public Perevoz() {}
    public Perevoz(int id, int autoId, int gruzId, int skladId, int driverId, Date date) {
        this.id = id;
        this.autoId = autoId;
        this.gruzId = gruzId;
        this.skladId = skladId;
        this.driverId = driverId;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAutoId() { return autoId; }
    public void setAutoId(int autoId) { this.autoId = autoId; }

    public int getGruzId() { return gruzId; }
    public void setGruzId(int gruzId) { this.gruzId = gruzId; }

    public int getSkladId() { return skladId; }
    public void setSkladId(int skladId) { this.skladId = skladId; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "Perevoz{id=" + id + ", autoId=" + autoId + ", gruzId=" + gruzId +
                ", skladId=" + skladId + ", driverId=" + driverId + ", date=" + date + "}";
    }
}
