package edu.brown.cs.mramesh4.GraphTest;

import edu.brown.cs.mramesh4.Graph.Graph;
import edu.brown.cs.mramesh4.TripGraph.CityEdge;
import edu.brown.cs.mramesh4.TripGraph.CityNode;
import edu.brown.cs.mramesh4.TripGraph.TripGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;

public class GraphTest {
  CityNode node;
  CityNode node2;
  CityNode node3;
  CityNode node4;
  CityNode node5;
  CityNode node6;
  private static final double DELTA = 0.01;
  TripGraph<CityNode, CityEdge> tripGraph = new TripGraph<>();

  @Before
  public void setUp() {
    node = new CityNode("New York", 40.4, -73.56);
    node2 = new CityNode("Jersey City", 40.34, -74.04);
    node3 = new CityNode("Trenton", 40.13, -74.46);
    node4 = new CityNode("Philadelphia", 39.57, -75.10);
    node5 = new CityNode("Pittsburgh", 40.27, -80);
    node6 = new CityNode("Harrison", 40.35, -79.6501);
    node.insertEdges(node2);
    node.insertEdges(node3);
    node.insertEdges(node4);
    node.insertEdges(node5);
    node.insertEdges(node6);
    node2.insertEdges(node3);
    node2.insertEdges(node5);
    node3.insertEdges(node4);
    node3.insertEdges(node5);
    node4.insertEdges(node5);
    node6.insertEdges(node4);
    List<CityNode> graphList = new ArrayList<>();
    graphList.add(node);
    graphList.add(node2);
    graphList.add(node3);
    graphList.add(node4);
    graphList.add(node5);
    graphList.add(node6);
    tripGraph = new TripGraph<>(graphList);
  }

  @After
  public void tearDown(){
    node = null;
    node2 = null;
    node3 = null;
    node4 = null;
    node5 = null;
    node6 = null;
    tripGraph = null;
  }

  @Test
  public void testAssumption(){
    //I built a graph example, I am making sure my math holds
    setUp();
    //NY --> JC: 0.483735
    assertEquals(0.483735, node.distanceBetween(node2), DELTA);
    //JC --> Trenton = 0.469574
    assertEquals(0.469574, node2.distanceBetween(node3), DELTA);
    //NY --> Trenton = 0.939628
    assertEquals(0.939628, node.distanceBetween(node3), DELTA);
    //Trenton --> Philadelphia: 0.850412
    assertEquals(0.850412, node3.distanceBetween(node4), DELTA);
    //NY -> Philly: 1.749428
    assertEquals(1.749428, node.distanceBetween(node4), DELTA);
    //NY --> Pitt = 6.441312
    assertEquals(6.441312, node.distanceBetween(node5), DELTA);
    //JC --> Pitt = 5.960411
    assertEquals(5.960411, node2.distanceBetween(node5), DELTA);
    //Trent --> Pitt = 5.541769
    assertEquals(5.541769, node3.distanceBetween(node5), DELTA);
    //Philly --> Pitt = 4.949747
    assertEquals(4.949747, node4.distanceBetween(node5), DELTA);
  }

  @Test
  public void testAStar(){
    setUp();
    tearDown();
  }

  @Test
  public void testConstructor(){
    setUp();
    assertEquals(tripGraph.getGraph().size(), 6, DELTA);
    tearDown();
  }

  @Test
  public void testDeleteNode(){
    setUp();
    assertEquals(tripGraph.getGraph().size(), 6, DELTA);
    //try to delete this again.
    tripGraph.deleteNode(node4);
    assertEquals(tripGraph.getGraph().size(), 5, DELTA);
    //if it doesn't exist it shouldnt error have an effect
    tripGraph.deleteNode(node4);
    assertEquals(tripGraph.getGraph().size(), 5, DELTA);
    tripGraph.deleteNode(node3);
    assertEquals(tripGraph.getGraph().size(), 4, DELTA);
    tearDown();
  }

  @Test
  public void testInsertNode(){
    setUp();
    assertEquals(tripGraph.getGraph().size(), 6, DELTA);
    List<CityNode> graphList = new ArrayList<>();
    graphList.add(node);
    graphList.add(node2);
    graphList.add(node3);
    graphList.add(node4);
    graphList.add(node5);
    graphList.add(node6);

    CityNode node7 = new CityNode("Philadelphia", 39.57, -75.10);
    CityNode node8 = new CityNode("newname", 40.1, -78.10);
    //if we insert something that exists, it doesn't change
    tripGraph.insertNode(node7, graphList);
    assertEquals(tripGraph.getGraph().size(), 6, DELTA);
    //if we insert a new node, it should insert
    graphList.add(node7);
    tripGraph.insertNode(node8, graphList);
    assertEquals(7, tripGraph.getGraph().size(), DELTA);
    tearDown();
  }
}
