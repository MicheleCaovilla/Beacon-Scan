package it.unipd.mich.beaconscan;

/**
 * Created by mich on 07/01/15.
 */

public class MyBeacon {
    public Position position;
    private String distance;
    private double alpha;
    private String beac_numb;

    public MyBeacon(double x, double y, int d, double a, int n) {
        this(x, y, String.valueOf(d), a, n);
    }

    public MyBeacon(double x, double y, int d, double a, String n) {
        this(x, y, String.valueOf(d), a, n);
    }

    public MyBeacon(double x, double y, String d, double a, int n) {
        position = new Position(x, y);
        distance = d;
        alpha = a;
        beac_numb = Integer.valueOf(n).toString();
    }

    public MyBeacon(double x, double y, String d, double a, String n) {
        position = new Position(x, y);
        distance = d;
        alpha = a;
        beac_numb = n;
    }

    // public void resetDistance() {setDistance("nil");}


    //getter
    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getZ() {
        return position.getZ();
    }

    public String getDistance() {
        return distance;
    }

    public int getDistanceNum() {
        return Integer.parseInt(distance);
    }

    public double getAlpha() {
        return alpha;
    }

    String getBeac_numb() {
        return beac_numb;
    }

    public Position getPosition() {
        return position;
    }

    //setter
    public void setX(double x) {
        position.setX(x);
    }

    public void setY(double y) {
        position.setX(y);
    }

    public void setZ(double z) {
        position.setZ(z);
    }

    public void setDistance(String d) {
        distance = d;
    }

    public void setDistance(int d) {
        distance = String.valueOf(d);
    }

    public void setAlpha(double a) {
        alpha = a;
    }

    public void setBeac_numb(int n) {
        beac_numb = Integer.valueOf(n).toString();
    }

}