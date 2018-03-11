package com.gmann;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.apache.commons.configuration.*;
import com.google.gson.Gson;
import java.util.Map;
import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {
  static Service s = null;
  private static Configuration config = null;

  @BeforeClass
  public static void init() throws Exception {
    try {config = new PropertiesConfiguration("neoapp.properties");} catch(Exception e){}
    s = new Service( config.getString("uri"), config.getString("username"), config.getString("password"));
  }

  @Test
  public void testA_Add() {
    Employee test = new Employee("harry kane", 10);
    String response = s.addEmployee(test);
    Map res = new Gson().fromJson(response, Map.class);
    Assert.assertTrue(((String)res.get("status")).equals("ok"));
  }

  @Test
  public void testB_Get() {
    String response = s.getEmployee("10");
    Map res = new Gson().fromJson(response, Map.class);
    Assert.assertTrue(((String)res.get("status")).equals("ok"));
    Assert.assertEquals(((ArrayList)res.get("results")).size(), 1);
    Assert.assertEquals(Math.round(((Double)((Map)((ArrayList)res.get("results")).get(0)).get("empid"))), 10);
  }
  
  @Test
  public void testC_Duplicate() {
    Employee test = new Employee("dele alli", 10);
    String response = s.addEmployee(test);
    Map res = new Gson().fromJson(response, Map.class);
    Assert.assertFalse(((String)res.get("status")).equals("ok"));
    Assert.assertTrue(((String)res.get("reason")).contains("exists"));
  }

  @Test
  public void testD_Del() {
    String response = s.delEmployee("10");
    Map res = new Gson().fromJson(response, Map.class);
    Assert.assertTrue(((String)res.get("status")).equals("ok"));
    response = s.getEmployee("10");
    res = new Gson().fromJson(response, Map.class);
    Assert.assertFalse(((String)res.get("status")).equals("ok"));
    Assert.assertTrue(((String)res.get("reason")).contains("found"));
  }
}
