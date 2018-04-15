package com.gen;

import com.gen.datastructures.Graph;
import com.gen.datastructures.parsers.ParserEdges;
import com.gen.datastructures.renderengine.EngineSwing;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        ParserEdges parserEdges = new ParserEdges();
        parserEdges.parse("./res/DOROGI.TXT");
        //parserEdges.parse("./res/REKI.TXT");
        parserEdges.fillGraph(graph);

        graph.setRenderEngine(new EngineSwing(1));
        graph.start(30);

        parserEdges.printGraphIn("curves1.txt", graph);
        System.out.println("end");
    }
}
