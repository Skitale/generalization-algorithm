package com.gen.datastructures.untils;

public class SimpleEdge {
    private PointInt A;
    private PointInt B;

    public SimpleEdge(int x1, int y1, int x2, int y2) {
        A = new PointInt(x1, y1);
        B = new PointInt(x2, y2);
    }

    public int getX1() {
        return A.getX();
    }

    public int getY1() {
        return A.getY();
    }

    public int getX2() {
        return B.getX();
    }

    public int getY2() {
        return B.getY();
    }

    public PointInt getPointA(){
        return new PointInt(A.getX(), A.getY());
    }

    public PointInt getPointB(){
        return new PointInt(B.getX(), B.getY());
    }

    @Override
    public String toString() {
        return "SimpleEdge{" +
                "x1=" + A.getX() +
                ", y1=" + A.getY() +
                ", x2=" + B.getX() +
                ", y2=" + B.getY() +
                '}';
    }
}