package com.gen.datastructures.untils;

import com.gen.datastructures.Graph;

import java.util.List;

public class Edge implements Comparable<Edge>{
    private Vertex first;
    private Vertex second;
    private int numCur;
    private double[][] matrixQ;
    private double optX;
    private double optY;
    private double priceContraction;
    private Graph context;
    private boolean isNeedCalc = true;
    private double mn = 1;

    public Edge(Vertex first, Vertex second, int numCur, Graph cnxt) {
        this.first = first;
        this.second = second;
        this.matrixQ = new double[3][3];
        this.context = cnxt;
        this.numCur = numCur;
        first.addEdge(this);
        second.addEdge(this);
        //cnxt.mergeCurve(first);
        //cnxt.mergeCurve(second);
    }

    public void upPc(int r){
        if(priceContraction == 0.0){
            priceContraction = Math.pow(10,4);
        }
        priceContraction*= priceContraction < 0 ? -10 : r;
    }

    public void upMn(double r){
        mn *= r;
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

    public List<Edge> getListEdgeForFirst(){
        return first.getEdgesList();
    }

    public List<Edge> getListEdgeForSecond(){
        return second.getEdgesList();
    }

    public Vertex getFirst() {
        return first;
    }

    public Vertex getSecond() {
        return second;
    }

    private double getQ11(){
        return matrixQ[0][0];
    }

    private double getQ12(){
        return matrixQ[0][1];
    }

    private double getQ1(){
        return matrixQ[0][2];
    }

    private double getQ22(){
        return matrixQ[1][1];
    }

    private double getQ2(){
        return matrixQ[1][2];
    }

    private double getQ0(){
        return matrixQ[2][2];
    }

    public int getNumCur() {
        return numCur;
    }

    public void calMatrix(){
        if (!isNeedCalc) return;
        //System.out.println("Cal Mat for {" + this + "}");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                double d = first.getMatrix(i,j) + second.getMatrix(i,j);
                if(mn > 1){
                    matrixQ[i][j] = d < 0 ? d / mn : d * mn;
                } else {
                    matrixQ[i][j] = d;
                }
            }
        }
    }

    private double function(double x, double y){
        return matrixQ[0][0]*x*x + 2*matrixQ[0][1]*x*y +  matrixQ[1][1]*y*y + 2*matrixQ[0][2]*x + 2 * matrixQ[1][2]*y + matrixQ[2][2];
    }

    private double dzdx(double x, double y){
        return 2*matrixQ[0][0]*x + 2*matrixQ[0][1]*y + 2*matrixQ[0][2];
    }

    private double dzdy(double x, double y){
        return 2*matrixQ[0][1]*x + 2*matrixQ[1][1]*y + 2*matrixQ[1][2];
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

    public void solveMin(){
        if(!isNeedCalc) return;
        double delta = 4*getQ11()*getQ22() - 4*Math.pow(getQ12(),2);
        double deltaX = -4*getQ1()*getQ22() + 4*getQ2()*getQ12();
        double deltaY = -4*getQ2()*getQ11() + 4*getQ1()*getQ12();
        double x_ = deltaX/delta; double y_ = deltaY/delta;

        double dz2dx2 = 2*getQ11(); double dz2dy2 = 2*getQ22(); double dz2dxdy = 2*getQ12();
        double delta_ = dz2dx2*dz2dy2 - Math.pow(dz2dxdy, 2);
        if(delta_ > 0 && dz2dx2 > 0){
            this.optX = x_ < 0 ? -x_ : x_; this.optY = y_ < 0 ? -y_ : y_;
        } else {
            //System.out.println("NOT fine: y1= "+ y1 + ", y2= " + y2 + ", [" + this.first.toString() + this.second.toString() + "]");
            double f1 = function(first.getX(), first.getY());
            double f2 = function(second.getX(), second.getY());

            if(f1 > f2){
                this.optX = second.getX(); this.optY = second.getY();
            } else {
                this.optX = first.getX(); this.optY = first.getY();
            }
        }

        this.priceContraction = function(this.optX, this.optY);
        this.isNeedCalc = false;
    }

    public void deleteEdge(){
        Vertex vertex = new Vertex((int) optX,(int)optY);
        vertex.setNumCur(numCur);
        context.addVertex(vertex);// + merge
        //context.mergeCurve(vertex);
        List<Edge> firstEdgeList = first.getEdgesList();
        List<Edge> secondEdgesList = second.getEdgesList();
        firstEdgeList.remove(this);
        secondEdgesList.remove(this);
        for(Edge edge: firstEdgeList){
            if(edge.first.getX() == this.first.getX() && edge.first.getY() == this.first.getY()){
                edge.first = vertex;
                context.deleteVertex(this.first);
            } else {
                edge.second = vertex;
                context.deleteVertex(this.second);
            }
            vertex.addEdge(edge);
            edge.isNeedCalc = true;
            edge.first.setNeedCalc();
            edge.second.setNeedCalc();
        }

        for(Edge edge: secondEdgesList){
            if(edge.first.getX() == this.second.getX() && edge.first.getY() == this.second.getY()){
                edge.first = vertex;
            } else {
                edge.second = vertex;
            }
            vertex.addEdge(edge);
            edge.isNeedCalc = true;
            edge.first.setNeedCalc();
            edge.second.setNeedCalc();
        }

        if(!this.contains(vertex)) {
            context.deleteVertex(this.first);
            context.deleteVertex(this.second);
        }
    }

    private boolean contains(Vertex v){
        return first.equals(v) || second.equals(v);
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
                ", Cur=" + numCur +
                ", priceContraction=" + priceContraction +
                '}';
    }
}
