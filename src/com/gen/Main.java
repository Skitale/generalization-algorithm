package com.gen;

import com.gen.datastructures.Graph;
import com.gen.datastructures.parsers.ParserEdges;
import com.gen.datastructures.untils.SimpleEdge;
import com.minigeo.MapWindow;
import com.minigeo.*;
import com.minigeo.Point;

import java.awt.*;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        ParserEdges parserEdges = new ParserEdges();
        parserEdges.parse("./res/curves1.txt");
        /*graph.addEdge(new Vertex(2,1),new Vertex(9,5));
        graph.addEdge(new Vertex(9,5), new Vertex(2,2));
        graph.addEdge(new Vertex(2,2), new Vertex(1,2));
        graph.addEdge(new Vertex(1,2), new Vertex(3,4));
        graph.addEdge(new Vertex(3,4), new Vertex(6,6));*/
        parserEdges.fillGraph( new Graph());
        parserEdges.fillGraph(graph);
        graph.endAdd();
        MapWindow mapWindow = new MapWindow();
        for(SimpleEdge se : graph.getSrcEdgeList()){
            mapWindow.addSegment(new Segment(new Point(se.getX1(), se.getY1()), new Point(se.getX2(),se.getY2()), Color.RED));
        }

        //mapWindow.addSegment(new Segment(new Point(1, 1), new Point(5,5), Color.RED));
        //mapWindow.addSegment(new Segment(new Point(5, 3), new Point(6,6), Color.RED));
        mapWindow.setVisible(true);
        //EngineDrawing engineDrawing = new EngineDrawing();
        //engineDrawing.createAndShowGui(graph.getSrcEdgeList());
        //engineDrawing.drawEdges(graph.getSrcEdgeList(),Color.RED);

        graph.calMatrixes();
        graph.sortOnPrice();
        graph.deleteEdges();
       /* for(SimpleEdge se : graph.getEdgeList()){
            mapWindow.addSegment(new Segment(new Point(se.getX1(), se.getY1()), new Point(se.getX2(),se.getY2()), Color.RED));
        }*/
        List<SimpleEdge> le = graph.desort();
        for(SimpleEdge se : le){
            mapWindow.addSegment(new Segment(new Point(se.getX1(), se.getY1()), new Point(se.getX2(),se.getY2()), Color.BLACK));
        }
        parserEdges.printIn("curves1.txt", graph.getSrcEdgeList(), le);
        //engineDrawing.drawEdges(graph.getEdgeList(), Color.BLACK);
        System.out.println("FFf");
    }
}
