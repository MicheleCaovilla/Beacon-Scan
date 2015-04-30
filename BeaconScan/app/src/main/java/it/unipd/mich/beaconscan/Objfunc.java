package it.unipd.mich.beaconscan;

import java.util.ArrayList;

public class Objfunc implements Objfun {

    final ArrayList<Position> positions;
    final ArrayList<Double> distances;

    public Objfunc(MyBeacon[] beacons) {
        ArrayList<Position> positions = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();

        for (MyBeacon b : beacons) {
            if (!b.getDistance().equals("nil")) {
                positions.add(b.getPosition());
                distances.add(b.getDistanceNum() * b.getAlpha());
            }
        }

        this.positions = positions;
        this.distances = distances;
    }

    private double distance(double p[], Position q) {
        double xSqr = (p[0] - q.getX()) * (p[0] - q.getX());
        double ySqr = (p[1] - q.getY()) * (p[1] - q.getY());
        double zSqr = (p[2] - q.getZ()) * (p[2] - q.getZ());
        return Math.sqrt(xSqr + ySqr + zSqr);
    }

    public double evalObjfun(double p0[]) {
        double sum = 0;

        for (int i = 0; i < positions.size(); ++i) {
            double delta = distance(p0, positions.get(i)) - distances.get(i);
            sum += delta * delta;
        }

        return sum / positions.size();
    }
}