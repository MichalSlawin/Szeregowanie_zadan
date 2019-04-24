package org.graph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.*;
import com.mxgraph.util.mxConstants;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import javax.swing.*;
import java.awt.*;

/**
 * A demo applet that shows how to use JGraphX to visualize JGraphT graphs. Applet based on
 * JGraphAdapterDemo.
 *
 */
public class JGraphXAdapter extends JApplet {

    private static final long serialVersionUID = 2202072534703043194L;

    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    private org.jgrapht.ext.JGraphXAdapter<Task, DefaultEdge> jgxAdapter;

    private Graph<Task, DefaultEdge> graph;

    public JGraphXAdapter(Graph<Task, DefaultEdge> graph) {
        this.graph = graph;
    }

    @Override
    public void init() {
        ListenableGraph<Task, DefaultEdge> g =
                new DefaultListenableGraph<>(this.graph);

        jgxAdapter = new org.jgrapht.ext.JGraphXAdapter<>(g);

        setPreferredSize(DEFAULT_SIZE);
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        component.getGraph().getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        getContentPane().add(component);
        resize(DEFAULT_SIZE);

        // thanks to Patryk Siewruk
        mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);

        layout.setIntraCellSpacing(50);
        layout.setInterRankCellSpacing(50);
        layout.setOrientation(SwingConstants.WEST);

        layout.execute(jgxAdapter.getDefaultParent());
    }
}