package pl.kurs.shapeapiapp.model;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@DiscriminatorValue("SQUARE")
public class Square extends Shape{
    private double height;
    @Formula("height * height")
    private double area;
    @Formula("4 * height")
    private double perimeter;

    public Square() {
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
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

    @Transient
    public double calculateArea() {
        return height * height;
    }

    @Transient
    public double calculatePerimeter() {
        return 4 * height;
    }

}
