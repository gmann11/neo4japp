package com.gmann;

import java.util.Map;
import java.util.HashMap;

public class Employee {
  private int empid;
  private String name;
 
  public Employee(String n, int id) {
    name = n;
    empid = id;
  }

  public int getId() {
    return empid;
  } 

  public void setEmpid(int id) {
    System.out.println("EMPID:" + id);
    empid = id;
  }

  public void setEmpid(String id) {
    empid = Integer.parseInt(id);
  }

  public void setName(String n) {
    name = n;
  }
  
  public String getName() {
    return name;
  } 

  public Map getParams() {
    Map<String,Object> params = new HashMap<>();
    params.put("name", name);
    params.put("empid", empid);
    return params;
  }

  public String toString() {
    return new StringBuilder().append("Employee=>name: ").append(name).append(" id:").append(empid).toString();
  }
}
