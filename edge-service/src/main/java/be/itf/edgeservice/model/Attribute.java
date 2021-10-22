package be.itf.edgeservice.model;

public class Attribute {
    private Integer id;
    private String name;
    private double scaleFactor;

    public Attribute() {
    }

    public Attribute(String name, double scaleFactor) {
        this.name = name;
        this.scaleFactor = scaleFactor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

}
