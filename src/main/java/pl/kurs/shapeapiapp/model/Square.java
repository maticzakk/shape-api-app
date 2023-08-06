package pl.kurs.shapeapiapp.model;


import javax.persistence.*;

@Entity
@DiscriminatorValue("SQUARE")
public class Square extends Shape {

    private double height;

    private double area;

    private double perimeter;

    @PrePersist
    @PreUpdate
    public void calculateAreaAndPerimeter() {
        area = height * height;
        perimeter = 4 * height;
    }


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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Square square = (Square) o;
//        return Double.compare(square.height, height) == 0 && Double.compare(square.area, area) == 0 && Double.compare(square.perimeter, perimeter) == 0;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(height, area, perimeter);
//    }
}
