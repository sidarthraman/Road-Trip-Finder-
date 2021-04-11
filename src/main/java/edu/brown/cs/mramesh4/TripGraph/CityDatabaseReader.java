package edu.brown.cs.mramesh4.TripGraph;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityDatabaseReader {

  private Connection conn;
  private String filepath;
  private List<String> cityList;
  private List<CityNode> cityNodes;


  public CityDatabaseReader(String filepath){
    cityList = new ArrayList<>();
    cityNodes = new ArrayList<>();
    this.filepath = filepath;
    this.setupConnection();
  }


  /**
   * Helper method that sets up the connection to the database.
   */
  private void setupConnection() {
    Connection c = null;
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: connection failed");
    }
    String url = "jdbc:sqlite:" + filepath;
    try {
      c = DriverManager.getConnection(url);
    } catch (SQLException e) {
      System.out.println("ERROR: connection failed");
    }
    conn = c;
  }

  public Connection connect(){
    return conn;
  }

  public void readDB(){
    PreparedStatement prep = null;

    try {
      prep = conn.prepareStatement("select city, state_id from cities;");
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
        String name = rs.getString(1) + ", " + rs.getString(2);
        cityList.add(name);
      }
      rs.close();
      prep.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void loadCityDB(){
    PreparedStatement prep = null;

    try {
      prep = conn.prepareStatement("select city, state_id, lat, lng from cities;");
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
        String name = rs.getString(1) + ", " + rs.getString(2);
        double lat = rs.getDouble(3);
        double longitude = rs.getDouble(4);
        CityNode curr = new CityNode(name, lat, longitude);
        cityNodes.add(curr);
      }
      rs.close();
      prep.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public List<String> getCities(){
    return cityList;
  }

  public List<CityNode> getNodes(){
    return cityNodes;
  }
}