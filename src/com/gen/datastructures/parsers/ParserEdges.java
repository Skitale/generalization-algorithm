package com.gen.datastructures.parsers;

import com.gen.datastructures.Graph;
import com.gen.datastructures.untils.PointInt;
import com.gen.datastructures.untils.SimpleEdge;
import com.gen.datastructures.untils.Vertex;

import java.io.*;
import java.util.*;

public class ParserEdges {
    private List<List<Vertex>> vertexList = new ArrayList<>();
    private int countCurves;
    private int[] countPointOfCurvers;

    @Deprecated
    public void parseExp(String file){
        int numOfCuv = 0;
        try {
            try (Scanner scanner = new Scanner(new FileReader(file)).useDelimiter("(\r\n)")) {
                while (scanner.hasNext()) {
                    if (scanner.hasNextInt()) {
                        numOfCuv++;
                    }
                    scanner.next();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(numOfCuv);
    }
    public void parse(String file){
        try {
            Scanner scanner = new Scanner(new FileReader(file)).useDelimiter("((\r\n)|\\s)");
            if(scanner.hasNextInt()){
                countCurves = scanner.nextInt();
                countPointOfCurvers = new int[countCurves];
                for(int i = 0; i < countCurves; i++){
                    vertexList.add(new ArrayList<>());
                }
            }

            int d;
            for(int i = 0; i < countCurves; i++){
                d = 0;
                if(scanner.hasNextInt()){
                    countPointOfCurvers[i] = scanner.nextInt();
                    d = countPointOfCurvers[i]*2;
                    float z = (countPointOfCurvers[i]%5.0f);
                    if(z != 0.0f){
                        d++;
                    }
                    d += countPointOfCurvers[i]/5;
                }else {
                    System.out.println(scanner.next());
                }
                List<Integer> list = new ArrayList<>();
                for(int j = 0; j < d; j++){
                    String str = scanner.next();
                    if(!str.startsWith("M")){
                        list.add(Integer.parseInt(str));
                    }
                    if(list.size() == 2){
                        vertexList.get(i).add(new Vertex(list.get(0), list.get(1)));
                        list.clear();
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printIn(String file, List<SimpleEdge> sourselse, List<SimpleEdge> lse){
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(file));
            Set<Integer> set = new HashSet<>();
            for(SimpleEdge e : sourselse){
                set.add(e.getNumCur());
            }
            int numCur = set.size();

            List<List<SimpleEdge>> listsEdges = new ArrayList<>();
            List<List<SimpleEdge>> listsSourceEdges = new ArrayList<>();
            for(int i = 0; i < numCur; i++){
                List<SimpleEdge> l = new ArrayList<>();
                List<SimpleEdge> sourcel = new ArrayList<>();
                for(SimpleEdge eg: lse) {
                    if (eg.getNumCur() == i) {
                        l.add(eg);
                    }
                }
                for(SimpleEdge eg: sourselse) {
                    if (eg.getNumCur() == i) {
                        sourcel.add(eg);
                    }
                }
                listsEdges.add(l);
                listsSourceEdges.add(sourcel);
            }

            printWriter.print(numCur*2);//2 before
            printWriter.println("");
            for(int i = 0; i< listsEdges.size(); i++){
                printOneCurvIn(printWriter,listsSourceEdges.get(i), listsEdges.get(i));
            }
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void printOneCurvIn(PrintWriter printWriter, List<SimpleEdge> sourselse, List<SimpleEdge> lse ){
            int counter = 0;
            boolean flagCycle = false;
            if(sourselse.get(0).getPointA().equals(sourselse.get(sourselse.size() - 1).getPointB())){
                counter++;
                flagCycle = true;

            }
            Set<PointInt> pointInts = new HashSet<>();
            for(SimpleEdge se: sourselse){
                pointInts.add(se.getPointA());
                pointInts.add(se.getPointB());
            }
            counter+= pointInts.size();
            printWriter.println(counter);
            printWriter.print("M(1):");
            int div = 0;
            for (SimpleEdge se : sourselse){
                if(div == 5){
                    printWriter.println("");
                    printWriter.print("M(1):");
                    div = 0;
                }
                printWriter.print(" " + se.getX1() + " " + se.getY1());
                div++;
            }

            if (flagCycle){
                if(div == 5){
                    printWriter.println("");
                    printWriter.print("M(1):");
                }
                printWriter.print(" " + sourselse.get(0).getX1() + " " + sourselse.get(0).getY1());
            }else {
                if(div == 5){
                    printWriter.println("");
                    printWriter.print("M(1):");
                }
                printWriter.print(" " + sourselse.get(sourselse.size() - 1).getX2() + " " + sourselse.get(sourselse.size() - 1).getY2());
            }

            printWriter.println("");
            pointInts.clear();
            counter = 0;
            flagCycle = false;
            div = 0;

            if(lse.get(0).getPointA().equals(lse.get(lse.size() - 1).getPointB())){
                counter++;
                flagCycle = true;

            }
            for(SimpleEdge se: lse){
                pointInts.add(se.getPointA());
                pointInts.add(se.getPointB());
            }
            counter+= pointInts.size();
            printWriter.println(counter);
            printWriter.print("M(1):");
            for (SimpleEdge se : lse){
                if(div == 5){
                    printWriter.println("");
                    printWriter.print("M(1):");
                    div = 0;
                }
                printWriter.print(" " + se.getX1() + " " + se.getY1());
                div++;
            }

            if (flagCycle){
                if(div == 5){
                    printWriter.println("");
                    printWriter.print("M(1):");
                }
                printWriter.print(" " + lse.get(0).getX1() + " " + lse.get(0).getY1());
            } else {
                if(div == 5){
                    printWriter.println("");
                    printWriter.print("M(1):");
                }
                printWriter.print(" " + lse.get(lse.size() - 1).getX2() + " " + lse.get(lse.size() - 1).getY2());
            }
            printWriter.println("");
    }

    public void fillGraph(Graph g){
        for(int k = 0; k < vertexList.size(); k++) {
            List<Vertex> vVertexList = vertexList.get(k);
            //vVertexList.get(0).upMn(1000);
           //vVertexList.get(vVertexList.size() - 1).upMn(1000);
            for (int i = 1; i < vVertexList.size(); i++) {
                g.addEdge(vVertexList.get(i - 1), vVertexList.get(i), k);
            }
            /*if (countCurves != (currentCurves + 1)) {
                currentCurves++;
            }*/
        }
    }
}
