package com.gen.datastructures.untils;

import java.util.List;

public class PointInt {
    private int x;
    private int y;

    public PointInt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PointInt){
            PointInt p = (PointInt)obj;
            if((p.x == x) && (p.y == y)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        String s = Integer.toString(x) + Integer.toString(y);
        return s.hashCode();
    }

    public static int isExistIn(List<SimpleEdge> lse, PointInt p){
        int counter = 0;
        for(SimpleEdge se : lse){
            PointInt pA = se.getPointA(); PointInt pB = se.getPointB();
            if(pA.equals(p)){
                counter++;
            }

            if(pB.equals(p)){
                counter++;
            }
        }
        return counter;
    }
}
