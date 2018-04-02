package com.gen.datastructures.untils;

import com.gen.datastructures.Graph;

import java.util.List;

public class Edge implements Comparable<Edge>{
    private Vertex first;
    private Vertex second;
    private double[][] matrixQ;
    private double optX;
    private double optY;
    private double priceContraction;
    private Graph context;
    private boolean isNeedCalc = true;
    private int mn = 1;

    public Edge(Vertex first, Vertex second, Graph cnxt) {
        this.first = first;
        this.second = second;
        this.matrixQ = new double[3][3];
        this.context = cnxt;
        first.addInEdge(this);
        second.addInEdge(this);
    }

    public void upMn(int r){
        mn*=r;
    }
    public int getX1(){
        return first.getX();
    }

    public int getY1(){
        return first.getY();
    }

    public int getX2(){
        return second.getX();
    }

    public int getY2(){
        return second.getY();
    }

    public PointInt getA(){
        return new PointInt(getX1(), getY1());
    }

    public PointInt getB(){
        return new PointInt(getX2(), getY2());
    }
    public void calMatrix(){
        if(!isNeedCalc) return;
        System.out.println("Cal Mat for {" + this + "}");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                double d = first.getMatrix(i,j) + second.getMatrix(i,j);
                if(d < 0) mn*=-1;
                matrixQ[i][j] = d * mn;
            }
        }
    }

    public double function(double x, double y){
        return matrixQ[0][0]*x*x + 2*matrixQ[0][1]*x*y +  matrixQ[1][1]*y*y + 2*matrixQ[0][2]*x + 2 * matrixQ[1][2]*y + matrixQ[2][2];
    }

    private Point minFun(double stX, double stY){
        double dx = 0.01; double dy = 0.01;
        double minX = 0, minY = 0;
        double min = Double.MAX_VALUE;
        for(double xi = stX - 100; xi < stX + 100; xi+=dx){
            for(double yi = stY - 100; yi < stY + 100; yi+=dy){
                if(function(xi,yi) < min){
                    min = function(xi,yi);
                    minX=xi; minY=yi;
                }
            }
        }
        return new Point(minX, minY);
    }

    public void solveMinimizationFunction(){
        if(!isNeedCalc) return;
        double y1;
        double y2;
        double y1_ = (matrixQ[1][1]*matrixQ[0][0] - matrixQ[0][1]*matrixQ[0][1]);
        double y2_ = (matrixQ[0][0]*matrixQ[1][2] - matrixQ[0][2]*matrixQ[0][1]);
        boolean nullDiv = false;
        if(y1_!=0.0f){
             y1 = (matrixQ[0][1]*matrixQ[0][2] - matrixQ[0][0]*matrixQ[2][1])/y1_;
        } else {
            y1 = Float.MAX_VALUE;
            nullDiv = true;
        }
        if(y2_!=0.0f){
            y2 = (matrixQ[0][2]*matrixQ[0][2] - matrixQ[0][0]*matrixQ[2][2])/y2_;
        } else {
            y2 = Float.MAX_VALUE - Float.MAX_VALUE/2;
            nullDiv = true;
        }

        if((y1 == y2) && !nullDiv){
            //System.out.println("all is fine!");
            double x = -y1*(matrixQ[0][1]/matrixQ[0][0])-(matrixQ[0][2]/matrixQ[0][0]);
            this.optX = x;
            this.optY = y1;
        } else {
            //System.out.println("NOT fine: y1= "+ y1 + ", y2= " + y2 + ", [" + this.first.toString() + this.second.toString() + "]");
            double f1 = function(first.getX(), first.getY());
            double f2 = function(second.getX(), second.getY());
            //Point p = minFun(first.getX(), first.getY());
            //System.out.println("MY OPT: optX= "+ p.getX() + ", optY= " + p.getY());
            if(f1 > f2){
                this.optX = second.getX(); this.optY = second.getY();
            } else {
                this.optX = first.getX(); this.optY = first.getY();
            }
        }
        //System.out.println("OPT: optX= "+ optX + ", optY= " + optY);
        this.priceContraction = function(this.optX, this.optY);
        this.isNeedCalc = false;
    }

    public void deleteEdge(){
        Vertex vertex = new Vertex((int) optX,(int)optY);
        context.addVertex(vertex);

        List<Edge> firstEdgeList = first.getEdgesList();
        List<Edge> secondEdgesList = second.getEdgesList();
        firstEdgeList.remove(this);
        secondEdgesList.remove(this);

        for(Edge edge: firstEdgeList){
            if(edge.first == this.first){
                edge.first = vertex;
            } else {
                edge.second = vertex;
            }
            vertex.addInEdge(edge);
            edge.isNeedCalc = true;
            edge.first.setNeedCalc();
            edge.second.setNeedCalc();
        }

        for(Edge edge: secondEdgesList){
            if(edge.first == this.second){
                edge.first = vertex;
            } else {
                edge.second = vertex;
            }
            vertex.addInEdge(edge);
            edge.isNeedCalc = true;
            edge.first.setNeedCalc();
            edge.second.setNeedCalc();
            context.deleteVertex(this.first);
            context.deleteVertex(this.second);
        }
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.priceContraction, o.priceContraction);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "first=" + first +
                ", second=" + second +
                ", optX=" + optX +
                ", optY=" + optY +
                ", priceContraction=" + priceContraction +
                '}';
    }

    /*public void addFirstVer(Vertex vertex){
        this.first = vertex;
        first.addInEdge(this);
    }

    public void addSecondVer(Vertex vertex){
        this.second = vertex;
        second.addInEdge(this);
    }*/
}
