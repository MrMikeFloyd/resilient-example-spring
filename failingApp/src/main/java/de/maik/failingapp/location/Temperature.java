package de.maik.failingapp.location;

public class Temperature {
    private double reading;
    private String scale;

    public Temperature(double reading, String scale) {
        this.reading = reading;
        this.scale = scale;
    }

    public double getReading() {
        return reading;
    }

    public String getScale() {
        return scale;
    }
}
