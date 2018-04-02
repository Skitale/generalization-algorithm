package com.gen.datastructures.untils;


import java.util.LinkedList;
import java.util.List;

public class Vertex{
    private int x;
    private int y;
    private double[][] matrixQ;
    private List<Edge> edgesList;
    private boolean isNeedCalc = true;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
        this.matrixQ = new double[3][3];
        this.edgesList = new LinkedList<>();
    }

    public void addInEdge(Edge edge){
        edgesList.add(edge);
    }

    public void calMatrix(){
        if(!isNeedCalc) return;
        System.out.println("Cal Mat for {" + this + "}");
        double q11 = 0; double q12 = 0; double q1 = 0;
        double q22 = 0; double q2 = 0; double q0 = 0;
        for(Edge edge : edgesList){
            double A , B, C;
            int x1 = edge.getX1(); int x2 = edge.getX2();
            int y1 = edge.getY1(); int y2 = edge.getY2();
            A = y2 - y1;
            B = x1 - x2;
            C = (x2 - x1) * y1 - (y2 - y1) * x1;
            double r = Math.pow(A,2) + Math.pow(B,2);
            if(r != 1){
                double z = 1/Math.sqrt(r);
                if(C > 0) {
                    z *= -1;
                }
                A *= z; B *= z; C *= z;
            }
            q11 += A * A;
            q12 += A * B;
            q22 += B * B;
            q1 += A * C;
            q2 += B * C;
            q0 += C * C;
        }
        matrixQ[0][0] = q11; matrixQ[0][1] = q12; matrixQ[0][2] = q1;
        matrixQ[1][0] = q12; matrixQ[1][1] = q22; matrixQ[1][2] = q2;
        matrixQ[2][0] = q1; matrixQ[2][1] = q2; matrixQ[2][2] = q0;
        this.isNeedCalc = false;
    }

    public double getMatrix(int i, int j){
        return matrixQ[i][j];
    }

    public List<Edge> getEdgesList() {
        return edgesList;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setNeedCalc(){
        this.isNeedCalc = true;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
