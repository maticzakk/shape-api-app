package pl.kurs.shapeapiapp.model;



import javax.persistence.*;

@Entity
@DiscriminatorValue("CIRCLE")
public class Circle extends Shape {
    private double radius;

    //@Formula("3.14 * radius * radius")
    private double area;
    //@Formula("3.14 * radius")
    private double perimeter;

    @PrePersist
    @PreUpdate
    public void calculateAreaAndPerimeter() {
        area = 3.14 * radius * radius;
        perimeter = 3.14 * radius;
    }

    public Circle(double radius) {
        this.radius = radius;
    }

    public Circle() {
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }

}
