package com.gen.datastructures;

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
            srcEdgeList.add(new SimpleEdge(edge.getX1(),edge.getY1(),edge.getX2(),edge.getY2(), edge.getNumCur()));
        }
        int numCur = getNumCur(edgeList);
        List<List<Edge>> lists = divByNumCur(edgeList,numCur);
        for(List<Edge> l : lists){
            l.get(0).upMn(2);
            l.get(l.size()-1).upMn(2);
        }
        //edgeList.get(0).upMn(10000);
        //edgeList.get(edgeList.size() - 1).upMn(10000);
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
        int numCur = vertex.getNumCur();
        for(Vertex ver : vertexList){
            if((ver.getX() == x) && (ver.getY() == y) && (ver.getNumCur() == numCur)){
                return ver;
            }
        }
        return null;
    }

    public void addEdge(Vertex vertex1, Vertex vertex2, int numCur){
        vertex1.setNumCur(numCur); vertex2.setNumCur(numCur);
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
        Edge edge = new Edge(vertexIn1, vertexIn2, numCur, this);
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
            edge.solveMin();
        }
        flagEnable = false;
    }


    public void deleteEdges(){
        float proc = 45.0f;
        float size = edgeList.size();
        while (((edgeList.size() * 100)/size) > proc){
            System.out.println((edgeList.size() * 100)/size);
            Edge edge = edgeList.get(0);
            flagEnable = true;
            if(getNumEdgeOfCurves(edge) != 1) {
                edge.deleteEdge();
                edgeList.remove(edge);
            } else {
                edge.upPc(10);
            }
            this.calMatrixes();
            this.sortOnPrice();
        }
    }

    private int getNumEdgeOfCurves(Edge e){
        int num = 0;
        for(Edge ed : edgeList){
            if(e.getNumCur() == ed.getNumCur()){
                num++;
            }
        }
        return num;
    }

    public List<SimpleEdge> getEdgeList(){
        List<SimpleEdge> list = new ArrayList<>();
        for(Edge edge : edgeList){
            list.add(new SimpleEdge(edge.getX1(),edge.getY1(),edge.getX2(), edge.getY2(), edge.getNumCur()));
        }
        return list;
    }

    public List<SimpleEdge> getSrcEdgeList(){
        return srcEdgeList;
    }

    public void sortOnPrice(){
        Collections.sort(edgeList);
    }

    private List<SimpleEdge> desort(List<Edge> edgeList){
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
                    le.add(new SimpleEdge(x1, y1, x2, y2, edgeList.get(k).getNumCur()));
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
        le.add(new SimpleEdge(x1, y1, x2, y2, edgeList.get(k).getNumCur()));
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
                    int X1 = edgeList.get(i).getX1();
                    int Y1 = edgeList.get(i).getY1();
                    int X2 = edgeList.get(i).getX2();
                    int Y2 = edgeList.get(i).getY2();
                    if ((x1 == X2) && (y1 == Y2)) {
                        les.add(0, new SimpleEdge(X1, Y1, X2, Y2, edgeList.get(i).getNumCur()));
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

    public static int getNumCur(List<Edge> list){
        Set<Integer> set = new HashSet<>();
        for(Edge e : list){
            set.add(e.getNumCur());
        }
        return set.size();
    }

    public static List<List<Edge>> divByNumCur(List<Edge> list, int numCur){
        List<List<Edge>> listsEdges = new ArrayList<>();
        for(int i = 0; i < numCur; i++){
            List<Edge> l = new ArrayList<>();
            for(Edge eg: list) {
                if (eg.getNumCur() == i) {
                    l.add(eg);
                }
            }
            listsEdges.add(l);
        }
        return listsEdges;
    }

    public List<SimpleEdge> getFormattedEdgeList(){
        int numCur = getNumCur(edgeList);

        List<List<Edge>> listsEdges = divByNumCur(edgeList, numCur);
        List<SimpleEdge> result = new ArrayList<>();

        //List<SimpleEdge> l = sortFormat(listsEdges.get(27));

        for(List<Edge> listE : listsEdges){
            List<SimpleEdge> t = sortFormat(listE);
            System.out.println(listsEdges.indexOf(listE));
            if(t.size()!=listE.size()){
                System.out.println("Warning...");
            }
            result.addAll(t);
        }

        return result;
    }

    private List<SimpleEdge> sortFormat(List<Edge> listE){
        List<SimpleEdge> res = new ArrayList<>();
        Vertex root = null;

        for(Edge e: listE){
            if(e.getListEdgeForFirst().size() == 1){
                root = e.getFirst();
                break;
            }
        }
        if(listE.get(0).getFirst().equals(listE.get(listE.size()-1).getSecond()) || root == null){
            return desort(listE);
        }

        Edge edgeCurrent = root.getEdgesList().get(0);
        for(Edge e: root.getEdgesList()){
            if(e.getFirst() == root){
                edgeCurrent = e;
            }
        }

        int x1 = edgeCurrent.getX1();
        int y1 = edgeCurrent.getY1();
        int x2 = edgeCurrent.getX2();
        int y2 = edgeCurrent.getY2();
        int numCur = edgeCurrent.getNumCur();
        res.add(new SimpleEdge(x1, y1, x2, y2, numCur));

        Vertex currentVertx = edgeCurrent.getSecond();
        while (edgeCurrent.getListEdgeForSecond().size() != 1 ){
            for(Edge e : edgeCurrent.getListEdgeForSecond()){
                if(e == edgeCurrent) continue;
                if(currentVertx == e.getFirst()){
                    currentVertx = e.getSecond();
                    edgeCurrent = e;
                } else {
                    currentVertx = e.getFirst();
                    edgeCurrent = e;
                }
            }
            x1 = edgeCurrent.getX1();
            y1 = edgeCurrent.getY1();
            x2 = edgeCurrent.getX2();
            y2 = edgeCurrent.getY2();
            numCur = edgeCurrent.getNumCur();
            res.add(new SimpleEdge(x1, y1, x2, y2, numCur));
        }

        return res;
    }
}

