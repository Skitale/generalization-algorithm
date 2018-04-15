package com.gen.datastructures;

import com.gen.datastructures.renderengine.EngineSwing;
import com.gen.datastructures.untils.Edge;
import com.gen.datastructures.untils.SimpleEdge;
import com.gen.datastructures.untils.Vertex;

import java.util.*;
import java.util.function.Predicate;

public class Graph {
    private List<Vertex> vertexList = new ArrayList<>();
    private List<SimpleEdge> srcEdgeList = new ArrayList<>();
    private List<Edge> edgeList = new ArrayList<>();
    private boolean flagEnable = true;
    private EngineSwing firstRenderEngine;
    private EngineSwing secondRenderEngine;

    //TODO: SUPPORTING FLAT GRAPH. AS GOT A INCORRECT RESULT
    public Graph() {

    }

    private void endAdd(){
        for(Edge edge: edgeList){
            srcEdgeList.add(new SimpleEdge(edge.getX1(),edge.getY1(),edge.getX2(),edge.getY2(), edge.getNumCur()));
        }
        int numCur = getNumCur(edgeList);
        List<List<Edge>> lists = divByNumCur(edgeList,numCur);
        for(List<Edge> l : lists){
            l.get(0).upMn(2);
            l.get(l.size()-1).upMn(2);
        }
    }

    public void start(int levelDegree){
        if(levelDegree > 100) levelDegree = 90;
        if(levelDegree < 0) levelDegree = 10;
        endAdd();
        calMatrixes();
        sortOnPrice();
        deleteEdges(levelDegree);

        if(firstRenderEngine != null && secondRenderEngine != null){
            firstRenderEngine.setSrcEdgeList(getSrcEdgeList());
            secondRenderEngine.setEdgeList(getEdgeList());
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    secondRenderEngine.setVisible(true);
                }
            });
            t.start();
            firstRenderEngine.setVisible(true);
        } else if(firstRenderEngine!=null) {
            firstRenderEngine.setEdgeList(getEdgeList());
            firstRenderEngine.setSrcEdgeList(getSrcEdgeList());
            firstRenderEngine.setVisible(true);
        }
    }

    public void setRenderEngine(EngineSwing engine){
        if(firstRenderEngine!=null) return;
        this.firstRenderEngine = engine;
    }

    public void setRenderEngine(EngineSwing fEngine, EngineSwing sEngine){
        if(firstRenderEngine != null && secondRenderEngine != null) return;
        this.firstRenderEngine = fEngine;
        this.secondRenderEngine = sEngine;
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

    public void mergeCurve(Vertex v){
        int x = v.getX(); int y = v.getY();
        List<Edge> listV = v.getEdgesList();
        Vertex resV = null;
        for(Vertex vertex : vertexList){
            if((vertex.getX() == x) && (vertex.getY() == y) && (!vertex.equals(v))){
                resV = vertex;
            }
        }
        if(resV == null) return;

        List<Edge> listVertex = resV.getEdgesList();
        for(Edge eV1 : listVertex){
            Vertex v_ = new Vertex(v.getX(), v.getY());
            v_.addEdge(eV1);
            vertexList.add(v_);
        }
        for (Edge eV2 : listV){
            Vertex v_ = new Vertex(resV.getX(), resV.getY());
            v_.addEdge(eV2);
            vertexList.add(v_);
        }
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

    private void calMatrixes(){
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


    private void deleteEdges(float proc){
        float size = edgeList.size();
        while (((edgeList.size() * 100)/size) > proc){
            System.out.println( "Process: " + (edgeList.size() * 100)/size + " %");
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

    public void clear(){
        /*for(Vertex v : vertexList){
            int num = v.getNumCur();
            List<Edge> edges = v.getEdgesList();
            edges.removeIf(new Predicate<Edge>() {
                @Override
                public boolean test(Edge edge) {
                    return edge.getFirst().getNumCur() != num || edge.getSecond().getNumCur() != num;
                }
            });
            System.out.println(edges.toString() + "\n" + v.getEdgesList().toString());
        }*/

        for(Vertex v: vertexList){
            List<Edge> edgeList = v.getEdgesList();
            edgeList.removeIf(new Predicate<Edge>() {
                @Override
                public boolean test(Edge e) {
                    return e.getFirst().equals(v) || e.getSecond().equals(v);
                }
            });
        }

        for(Edge e: edgeList){
            int num = e.getNumCur();
            int snum = e.getSecond().getNumCur();
            int fnum = e.getFirst().getNumCur();
            if(num != snum){
                e.getSecond().setNumCur(num);
                System.out.println(e);
            } else if(snum != fnum){
                e.getFirst().setNumCur(num);
                //list.remove(e);
                System.out.println(e);
            }
        }

        for(int i = 0 ; i < vertexList.size(); i++){
            Vertex v = vertexList.get(i);
            vertexList.removeIf(new Predicate<Vertex>() {
                @Override
                public boolean test(Vertex vertex) {
                    int x = vertex.getX(); int y = vertex.getY();
                    return v.getX() == x && v.getY() == y && v != vertex;
                }
            });
        }

        for(Vertex v : vertexList){
            int num = v.getNumCur();
            List<Edge> edges = v.getEdgesList();
            edges.removeIf(new Predicate<Edge>() {
                @Override
                public boolean test(Edge edge) {
                    return edge.getFirst().getNumCur() != num || edge.getSecond().getNumCur() != num;
                }
            });
            System.out.println(edges.toString() + "\n" + v.getEdgesList().toString());
        }
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
        if(root == null){ // then cycle
            return sortFormatForCycle(listE);
        }

        Edge edgeCurrent = root.getEdgesList().get(0);
        for(Edge e: root.getEdgesList()){
            if(e.getFirst() == root){
                edgeCurrent = e;
            }
        }

        addEdgeInList(res,edgeCurrent);
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
            addEdgeInList(res,edgeCurrent);
        }

        return res;
    }

    private List<SimpleEdge> sortFormatForCycle(List<Edge> listE) {
        List<SimpleEdge> res = new ArrayList<>();
        Vertex root = listE.get(0).getFirst();
        Edge edgeCurrent = null;
        for(Edge e: root.getEdgesList()){
            if(e.getFirst() == root){
                edgeCurrent = e;
            }
        }

        Edge edgeRoot = edgeCurrent;
        addEdgeInList(res, edgeRoot);
        edgeCurrent = null;

        Vertex currentVertx = edgeRoot.getSecond();
        while (edgeCurrent != edgeRoot){
            for(Edge e : currentVertx.getEdgesList()){
                if(e.getFirst() == currentVertx){
                    edgeCurrent = e;
                    currentVertx = e.getSecond();
                }
            }
            addEdgeInList(res, edgeCurrent);
        }
        res.remove(res.size()-1);
        return res;
    }

    private void addEdgeInList(List<SimpleEdge> list, Edge edge){
        if(edge==null) return;
        int x1 = edge.getX1();
        int y1 = edge.getY1();
        int x2 = edge.getX2();
        int y2 = edge.getY2();
        int numCur = edge.getNumCur();
        list.add(new SimpleEdge(x1, y1, x2, y2, numCur));
    }
}

