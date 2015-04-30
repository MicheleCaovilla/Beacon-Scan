package it.unipd.mich.beaconscan;

/**
 * Created by Mich on 16/03/15.
 */
public class Position {

    private double x, y, z;

    public Position(double x1, double y1) {
        this(x1, y1, 0);
    }


    public Position(double X, double Y, double Z) {
        setX(X);
        setY(Y);
        setZ(Z);
    }


    //SET
    public void setX(double X) {
        x = X;
    }

    public void setY(double Y) {
        y = Y;
    }

    public void setZ(double Z) {
        z = Z;
    }


    //GET
    public double getX() {
        return (x);
    }

    public double getY() {
        return (y);
    }

    public double getZ() {
        return (z);
    }


}
