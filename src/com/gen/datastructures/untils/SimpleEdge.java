package com.gen.datastructures.untils;

import java.util.List;

public class SimpleEdge {
    private PointInt A;
    private PointInt B;
    private int numCur;

    public SimpleEdge(int x1, int y1, int x2, int y2, int numCur) {
        A = new PointInt(x1, y1);
        B = new PointInt(x2, y2);
        this.numCur = numCur;
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

    public int getNumCur() {
        return numCur;
    }

    public static int findMinX(List<SimpleEdge> simpleEdges){
        int minX = simpleEdges.get(0).getX1() < simpleEdges.get(0).getX2() ? simpleEdges.get(0).getX1() : simpleEdges.get(0).getX2();
        for(SimpleEdge e : simpleEdges){
            int curMin = e.getX1() < e.getX2() ? e.getX1() : e.getX2();
            if(curMin < minX){
                minX = curMin;
            }
        }
        return minX;
    }

    public static int findMinY(List<SimpleEdge> simpleEdges){
        int minY = simpleEdges.get(0).getY1() < simpleEdges.get(0).getY2() ? simpleEdges.get(0).getY1() : simpleEdges.get(0).getY2();
        for(SimpleEdge e : simpleEdges){
            int curMin = e.getY1() < e.getY2() ? e.getY1() : e.getY2();
            if(curMin < minY){
                minY = curMin;
            }
        }
        return minY;
    }

    @Override
    public String toString() {
        return "SimpleEdge{" +
                "x1=" + A.getX() +
                ", y1=" + A.getY() +
                ", x2=" + B.getX() +
                ", y2=" + B.getY() +
                ", Cur=" + numCur +
                '}';
    }
}
