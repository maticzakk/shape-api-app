package pl.kurs.shapeapiapp.model;


import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("SQUARE")
public class Square extends Shape implements Serializable {

    private double height;
    private double area;
    private double perimeter;

    public Square(double height) {
        this.height = height;
    }

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
}
