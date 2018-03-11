package com.gmann;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.Values.*;
import org.neo4j.driver.v1.exceptions.ClientException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.configuration.*;
import org.neo4j.helpers.collection.MapUtil;
import com.google.gson.Gson;

public class Service implements AutoCloseable {
  private final Driver driver;
  Logger logger = LoggerFactory.getLogger(Service.class);
  private static Configuration config = null;

  public Service() {
    try {
      config = new PropertiesConfiguration("neoapp.properties");
    } catch (ConfigurationException e) {
      logger.error("Configuration Error:", e);
    }
    driver = GraphDatabase.driver( config.getString("uri"), AuthTokens.basic( config.getString("username"), config.getString("password") ) );
  }

  public Service(String uri, String user, String password) {
    driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
  }

  @Override
  public void close() throws Exception {
    driver.close();
  }

  public String addEmployee(Employee emp) {
    StatementResult res = null;
    try ( Session session = driver.session() ) {
      res = session.run("CREATE (a:Employee {name: $name, emp_id: $empid})", emp.getParams() );
    } catch (ClientException ex ) {
      System.err.println( ex.getMessage() );
      return getResponse("failed", ex.getMessage(), null);
    }
    return getResponse("ok", null, null);
  }

  public String getEmployees() {
    return getResponse("ok", null, getEmployeesInt(null));
  }

  public String getEmployee(String id) {
    try {
      Integer x = Integer.valueOf(id);
      List<Employee> z = getEmployeesInt(x);
      if (z.size() == 0) {
        return getResponse("failed", "not found", null); 
      }
      return getResponse("ok", null, z);
    } catch (NumberFormatException ex) {
      logger.error( "Error deleting employee:", ex);
      return getResponse("failed", "invalid emp_id", null);
    } 
  }

  public String delEmployees() {
    try {
      List<Employee> z = delEmployeesInt(null);
      return getResponse("ok", null, null);
    } catch (NumberFormatException ex) {
      logger.error( "Error deleting employee:", ex);
      return getResponse("failed", "invalid emp_id", null);
    } 
  }

  public String delEmployee(String id) {
    try {
      Integer x = Integer.valueOf(id);
      List<Employee> z = delEmployeesInt(x);
      return getResponse("ok", null, null);
    } catch (NumberFormatException ex) {
      logger.error( "Error deleting employee:", ex);
      return getResponse("failed", "invalid emp_id", null);
    } 
  }
 
  private List<Employee> getEmployeesInt(Integer id) {
    String query = null;
    if (id != null) {
      query = "match (Employee) where Employee.emp_id = {id} return Employee";
    } else {
      query = "match (Employee) return Employee";
    }
    StatementResult result = null;
    List<Employee> res = new ArrayList();
    try ( Session session = driver.session() ) {
      result = session.run(query, MapUtil.map("id", id));
      for (Record record : result.list()) {
        res.add(new Employee(record.get("Employee").get("name").asString(),record.get("Employee").get("emp_id").asInt()));
      }
      return res;
    } 
  }

  private List<Employee> delEmployeesInt(Integer id) {
    String query = null;
    if (id != null) {
      query = "match (Employee) where Employee.emp_id = {id} detach delete Employee";
    } else {
      query = "match (Employee) detach delete Employee";
    }
    StatementResult result = null;
    List<Employee> res = new ArrayList();
    try ( Session session = driver.session() ) {
      result = session.run(query, MapUtil.map("id", id));
      for (Record record : result.list()) {
        res.add(new Employee(record.get("Employee").get("name").asString(),record.get("Employee").get("emp_id").asInt()));
      }
      return res;
    } 
  }

  private String getResponse(String status, String msg, List<Employee> respData) {
    Map res = new HashMap();
    res.put("status", status);
    if (status.equals("failed")) {
      res.put("reason", msg);
    }
    if (respData != null) {
      res.put("results", respData);
    }
    return new Gson().toJson(res);
  }
}
