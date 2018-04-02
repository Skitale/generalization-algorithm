package com.gen.datastructures.renderengine;

import com.gen.datastructures.untils.Edge;
import com.gen.datastructures.untils.SimpleEdge;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Deprecated
public class EngineSwing extends JFrame{
    private List<SimpleEdge> srcEdgeList;
    private List<Edge> edgeList;
    private Container container;
    private DrawComponent drawComponent;
    public EngineSwing(List<Edge> edgeList , List<SimpleEdge> srcEdgeList) throws HeadlessException {
        super("Grap");
        this.srcEdgeList = srcEdgeList;
        this.edgeList = edgeList;
        JPanel jcp = new JPanel(new BorderLayout());
        this.setBounds(0,0,6000,9000);
        //setSize(10000,10000);
        //this.setResizable(false);
        setContentPane(jcp);
        container = this.getContentPane();

        drawComponent = new DrawComponent(true);
        container.add(drawComponent, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    class DrawComponent extends JPanel{

        public DrawComponent(boolean isDoubleBuffered) {
            super(isDoubleBuffered);
        }

        @Override
        protected void paintComponent(Graphics gh) {
            Graphics2D drp = (Graphics2D)gh;
            int mn = 7;
            for(SimpleEdge edge : srcEdgeList){
                int x1 = edge.getX1(); int x2 = edge.getX2();
                int y1 = edge.getY1(); int y2 = edge.getY2();
                drp.setColor(Color.RED);
                drp.drawLine(x1*mn, y1*mn - 100, x2*mn, y2*mn - 100);
            }

            for(Edge edge : edgeList){
                int x1 = edge.getX1(); int x2 = edge.getX2();
                int y1 = edge.getY1(); int y2 = edge.getY2();
                drp.setColor(Color.BLACK);
                drp.drawLine(x1/mn, y1/mn, x2/mn, y2/mn);
            }
        }
    }
}
