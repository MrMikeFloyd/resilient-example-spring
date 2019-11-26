package de.maik.resilientapp.recommender.entity;

public class Temperature {
    private double reading;
    private String scale;

    public Temperature(){
    };

    public Temperature(double reading, String scale) {
        this.reading = reading;
        this.scale = scale;
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        this.reading = reading;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "reading=" + reading +
                ", scale='" + scale + '\'' +
                '}';
    }
}
