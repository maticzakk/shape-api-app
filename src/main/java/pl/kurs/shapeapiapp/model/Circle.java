package pl.kurs.shapeapiapp.model;


import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@DiscriminatorValue("CIRCLE")
public class Circle extends Shape {
    private double radius;

    @Formula("3.14 * radius * radius")
    private double area;
    @Formula("3.14 * radius")
    private double perimeter;

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

    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    public double calculatePerimeter() {
        return Math.PI * radius;
    }

}
