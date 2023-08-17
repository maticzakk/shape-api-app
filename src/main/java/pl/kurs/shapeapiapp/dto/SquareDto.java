package pl.kurs.shapeapiapp.dto;


public class SquareDto extends ShapeDto {

    private double height;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    public double getArea() {
        return height * height;
    }

    public double getPerimeter() {
        return 4 * height;
    }

}
