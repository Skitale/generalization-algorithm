package com.gen.datastructures.renderengine;

import com.gen.datastructures.untils.SimpleEdge;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class EngineSwing extends JFrame{
    private List<SimpleEdge> srcEdgeList;
    private List<SimpleEdge> edgeList;
    private Container container;
    private DrawComponent drawComponent;
    private int minX;
    private int minY;
    private int mn;

    public EngineSwing(int zoom) throws HeadlessException {
        super("Grap");
        JPanel jcp = new JPanel(new BorderLayout());
        this.setBounds(0,0,6000,9000);
        mn = zoom;
        setContentPane(jcp);
        container = this.getContentPane();

        drawComponent = new DrawComponent(true);
        container.add(drawComponent, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setEdgeList(List<SimpleEdge> edgeList) {
        this.edgeList = edgeList;
        minX = SimpleEdge.findMinX(edgeList) * mn;
        minY = SimpleEdge.findMinY(edgeList) * mn;
    }

    public void setSrcEdgeList(List<SimpleEdge> edgeList) {
        this.srcEdgeList = edgeList;
        minX = SimpleEdge.findMinX(edgeList) * mn;
        minY = SimpleEdge.findMinY(edgeList) * mn;
    }

    class DrawComponent extends JPanel{

        public DrawComponent(boolean isDoubleBuffered) {
            super(isDoubleBuffered);
        }

        @Override
        protected void paintComponent(Graphics gh) {
            Graphics2D drp = (Graphics2D)gh;
            int Cx = minX;
            int Cy = minY;
            if(srcEdgeList != null) {
                for (SimpleEdge edge : srcEdgeList) {
                    int x1 = edge.getX1();
                    int x2 = edge.getX2();
                    int y1 = edge.getY1();
                    int y2 = edge.getY2();
                    drp.setColor(Color.RED);
                    drp.drawLine(x1 * mn - Cx, y1 * mn - Cy, x2 * mn - Cx, y2 * mn - Cy);
                }
            }

            if(edgeList != null) {
                for (SimpleEdge edge : edgeList) {
                    int x1 = edge.getX1();
                    int x2 = edge.getX2();
                    int y1 = edge.getY1();
                    int y2 = edge.getY2();
                    drp.setColor(Color.BLACK);
                    drp.drawLine(x1 * mn - Cx, y1 * mn - Cy, x2 * mn - Cx, y2 * mn - Cy);
                }
            }
        }
    }
}
