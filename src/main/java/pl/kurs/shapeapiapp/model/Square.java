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

    @Override
    public double getArea() {
        return Math.pow(height, 2);
    }

    @Override
    public double getPerimeter() {
        return 4 * height;
    }
}
