package com.gen.datastructures;

import com.gen.datastructures.renderengine.EngineSwing;
import com.gen.datastructures.untils.Edge;
import com.gen.datastructures.untils.SimpleEdge;
import com.gen.datastructures.untils.Vertex;

import java.util.*;

public class Graph {
    private List<Vertex> vertexList = new ArrayList<>();
    private List<SimpleEdge> srcEdgeList = new ArrayList<>();
    private List<Edge> edgeList = new ArrayList<>();
    private boolean flagEnable = true;

    //TODO: SUPPORTING FLAT GRAPH. AS GOT A INCORRECT RESULT
    public Graph() {

    }

    public void endAdd(){
        for(Edge edge: edgeList){
            srcEdgeList.add(new SimpleEdge(edge.getX1(),edge.getY1(),edge.getX2(),edge.getY2()));
        }
        edgeList.get(0).upMn(10000);
        edgeList.get(edgeList.size() - 1).upMn(10000);
    }
    public void addVertex(Vertex vertex){
        if(hasVertex(vertex) == null) {
            vertexList.add(vertex);
        }
    }

    public void deleteVertex(Vertex vertex){
        if(!(hasVertex(vertex) == null)){
            vertexList.remove(vertex);
        }
    }

    private Vertex hasVertex(Vertex vertex){
        int x = vertex.getX(); int y = vertex.getY();
        for(Vertex ver : vertexList){
            if((ver.getX() == x) && (ver.getY() == y)){
                return ver;
            }
        }
        return null;
    }

    public void addEdge(Vertex vertex1, Vertex vertex2){
        Vertex vertexIn1 = hasVertex(vertex1);
        if(vertexIn1 == null){
            addVertex(vertex1);
            vertexIn1 = vertex1;
        }
        Vertex vertexIn2 = hasVertex(vertex2);
        if(vertexIn2 == null){
            addVertex(vertex2);
            vertexIn2 = vertex2;
        }
        Edge edge = new Edge(vertexIn1, vertexIn2, this);
        edgeList.add(edge);
    }

    public void calMatrixes(){
        if(!flagEnable){
            return;
        }
        for(Vertex vertex : vertexList){
            vertex.calMatrix();
        }

        for(Edge edge: edgeList){
            edge.calMatrix();
            edge.solveMinimizationFunction();
        }
        flagEnable = false;
    }


        public void deleteEdges(){
        float proc = 55.0f;
        float size = edgeList.size();
        while (((edgeList.size() * 100)/size) > proc){
            Edge edge = edgeList.get(0);
            edge.deleteEdge();
            flagEnable = true;
            edgeList.remove(edge);
            this.calMatrixes();
            this.sortOnPrice();
        }
    }

    public List<SimpleEdge> getEdgeList(){
        List<SimpleEdge> list = new ArrayList<>();
        for(Edge edge : edgeList){
            list.add(new SimpleEdge(edge.getX1(),edge.getY1(),edge.getX2(),edge.getY2()));
        }
        return list;
    }

    public List<SimpleEdge> getSrcEdgeList(){
        return srcEdgeList;
    }

    public void sortOnPrice(){
        Collections.sort(edgeList);
    }

    public List<SimpleEdge> desort(){
        List<SimpleEdge> le = new ArrayList<>();
        int k = 0;
        Edge root = edgeList.get(k);
        Edge cur = root;
        int si = -2;
        while(!root.getA().equals(cur.getB()) && !(si == edgeList.size() - 1)) {
            //System.out.println("size le: " + le.size());
            for (int i = 0; i < edgeList.size(); i++) {
                if(i == k){
                    si = i;
                    continue;
                }
                int x1 = edgeList.get(k).getX1();
                int y1 = edgeList.get(k).getY1();
                int x2 = edgeList.get(k).getX2();
                int y2 = edgeList.get(k).getY2();
                int X1 = edgeList.get(i).getX1();
                int Y1 = edgeList.get(i).getY1();
                int X2 = edgeList.get(i).getX2();
                int Y2 = edgeList.get(i).getY2();
                if ((x2 == X1) && (y2 == Y1) || (x2 == X2)&&(y2 == Y2)) {
                    le.add(new SimpleEdge(x1, y1, x2, y2));
                    k = i;
                    cur = edgeList.get(k);
                    break;
                }
                si = i;
            }
        }
        int x1 = edgeList.get(k).getX1();
        int y1 = edgeList.get(k).getY1();
        int x2 = edgeList.get(k).getX2();
        int y2 = edgeList.get(k).getY2();
        le.add(new SimpleEdge(x1, y1, x2, y2));
        if(le.size()!=edgeList.size()) {
            List<SimpleEdge> les = new ArrayList<>();
            k = 0;
            si = -2;
            while (!(si == edgeList.size() - 1)){
                //System.out.println("size les: " + les.size());
                for (int i = 0; i < edgeList.size(); i++) {
                    if(i == k){
                        si = i;
                        continue;
                    }
                    x1 = edgeList.get(k).getX1();
                    y1 = edgeList.get(k).getY1();
                    x2 = edgeList.get(k).getX2();
                    y2 = edgeList.get(k).getY2();
                    int X1 = edgeList.get(i).getX1();
                    int Y1 = edgeList.get(i).getY1();
                    int X2 = edgeList.get(i).getX2();
                    int Y2 = edgeList.get(i).getY2();
                    if ((x1 == X2) && (y1 == Y2)) {
                        les.add(0, new SimpleEdge(X1, Y1, X2, Y2));
                        k = i;
                        break;
                    }
                    si = i;
                }
            }
            les.addAll(le);
            if (les.size() != edgeList.size()){
                System.out.println("WARNING! WARNING!");
            }
            return les;
        }
        return le;
    }
}
