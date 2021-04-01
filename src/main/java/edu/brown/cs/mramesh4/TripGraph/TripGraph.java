package edu.brown.cs.mramesh4.TripGraph;


import edu.brown.cs.mramesh4.Graph.GraphNodeComparator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This is a class representing a Graph with nodes with undirected edges. We represent
 * undirected edges by just doubling direction.
 * @param <N> Type of Node used [Will be CityNode]
 * @param <E> Type of Node used [Will be CityEdge]
 */
public class TripGraph<N extends TripGraphNode<N, E>, E extends TripGraphEdge<N, E>>{
    private HashMap<String, N> graph;

  /**
   * Empty constructor for a graph with no nodes.
   */
  public TripGraph() {
    graph = new HashMap<>();
  }

  /**
   * This is a constructor for a graph with a list of nodes
   * If duplicates add the node
   * @param nodes list of nodes
   */
  public TripGraph(List<N> nodes){
    //fill our list of nodes
    graph = new HashMap<>();
      for(N node: nodes){
        String name = node.getName();
        if(!graph.containsKey(name)){
          graph.put(name, node);
        }
      }
  }

  /**
   * This is a method to run aStar on a graph.
   * @param start the name of the start city
   * @param end the name of the end city.
   * @return a list of nodes to visit
   */
  public List<N> aStar(String start, String end){
    List<N> ret = new ArrayList<>();
    if(!graph.containsKey(start) || !graph.containsKey(end)){
      return null;
    } else if(start.equals(end)){
      ret.add(graph.get(start));
      return ret;
    } else{
      ret = aStarHelper(graph.get(start), graph.get(end));
    }
    return ret;
  }

  /**
   * This is a helper to run aStar within the graph that we are writing
   * @param start the start node to search from
   * @param end the end node to start from
   * @return a list of nodes
   */
  public List<N> aStarHelper(N start, N end){
    //create a comparator that sorts them by a weight aStar assigns them
    PriorityQueue<N> pq = new PriorityQueue<>(new TripGraphNodeComparator<N, E>());
    //set the distance travelled to 0
    start.setDistance(0);
    //keep track of a visited
    HashMap<String, N> visited = new HashMap<>();
    //put them in the visited
    visited.put(start.getName(), start);
    List<N> nodes = new ArrayList<>();
    nodes.add(start);
    aStarRecurs(end, pq, nodes, visited);
    return nodes;
  }

  /**
   * This is a recursive method to fill our aStar method and run aStar
   * @param end the goal node to reach
   * @param pq a priorityqueue of nodes left to visit
   * @param nodes a list of nodes in order to visit
   * @param visited a map of visited nodes
   */
  //TODO: Hand work through an example of aStarRecurs
  //This should work but I am not sure.
  public void aStarRecurs(N end, PriorityQueue<N> pq, List<N> nodes, HashMap<String, N> visited){
    if(!pq.isEmpty()){
      N curr = pq.poll();
      if (curr.equals(end)) {
        return;
      }
      List<N> neighbors = curr.getNeighbors();
      for(N next: neighbors){
        //we don't want to revisit this, because this will set an infinite loop
        if(next.equals(curr)) {
          continue;
        } else {
          String name = next.getName();
          //this is the distance of curr to edge of next
          Double dist = curr.getDistance() + curr.getConnectingEdges().get(name).getWeight();
          Double totalWeight = dist + next.toGoal(end);
          if (!visited.containsKey(next.getName())) {
            next.setDistance(dist);
            next.setWeight(totalWeight);
            visited.put(next.getName(), next);
            pq.add(next);
            nodes.add(next);
            //backtrack: add this to the pq and add it
            aStarRecurs(end, pq, nodes, visited);
            //not sure if i need to remove all this.
            visited.remove(next.getName());
            pq.remove(next);
            nodes.remove(next);
          } else {
            //checking if we want to update the weight and distance
            if (visited.get(next.getName()).getWeight() > totalWeight) {
              next.setDistance(dist);
              next.setWeight(totalWeight);
              visited.put(next.getName(), next);
              pq.add(next);
              nodes.add(next);
              //backtrack: add this to the pq and add it
              aStarRecurs(end, pq, nodes, visited);
              //remove it from the backtracking
              visited.remove(next.getName());
              pq.remove(next);
              nodes.remove(next);
            }
          }
        }
      }

    }
  }

  /**
   * Node to insert and a List of Nodes to have edges with.
   * If we put in a node we already have encountered, it will not reinsert.
   * @param node node to add to graph
   * @param nodes list of nodes to connect it to.
   */
  public void insertNode(N node, List<N> nodes){
    String nodeName = node.getName();

    if(!graph.containsKey(nodeName) || (graph.containsKey(nodeName) && !graph.get(nodeName).equals(node))) {
      //for all the nodes within the list of nodes to add to
      //make sure they are in the graph
      for (N neighbor : nodes) {
        String name = neighbor.getName();
        if (graph.containsKey(name)) {
          //add an edge between them
          node.insertEdges(neighbor);
        }
      }
      //if there is an edge between node and another node
      //in the graph, this is valid, push it to the graph
      if (!node.getOutgoingEdges().isEmpty()) {
        graph.put(node.getName(), node);
      }
    }
  }

  /**
   * This is how to delete a node from the graph.
   * @param node node to delete in the graph
   */
  public void deleteNode(N node){
    //delete from the list of graph
    String name = node.getName();
    graph.remove(name);
    //delete from each nodes
    for(N graph : graph.values()){
      graph.getConnectingEdges().remove(name);
      graph.getConnectingNodes().remove(name);
    }
  }

  /**
   * This method deletes an edge between two nodes
   * @param start one node to delete from
   * @param end end node to delete from
   */
  public void deleteEdge(N start, N end){
    start.deleteEdge(end);
  }

  /**
   * This method adds an edge between two nodes within the graph.
   * @param start one node to an add an edge from
   * @param end end node to add an edge from
   */
  public void insertEdge(N start, N end){
    start.insertEdges(end);
  }

  /**
   * Accesor method for graph.
   * @return a graph.
   */
  public HashMap<String, N> getGraph(){
    return graph;
  }
}
