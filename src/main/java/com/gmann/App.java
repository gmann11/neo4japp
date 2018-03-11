package com.gmann;

import static spark.Spark.*;
import spark.utils.IOUtils;
import spark.Spark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.*;

public class App {
    public static void main(String[] args) {
       Logger logger = LoggerFactory.getLogger(App.class);
       port(8080);
       staticFiles.location("/public");
       Service empService = new Service();

       get("/ui", (request, response) -> {
         return IOUtils.toString(Spark.class.getResourceAsStream("/public/home.html"));
       });

       get("/employees", (req, res) -> {
         res.type("application/json");
         String json = empService.getEmployees();
         return json;
       });

       get("/del/:id", (req, res) -> {
         res.type("application/json");
         String id = req.params(":id");
         String json = empService.delEmployee(id);
         return json;
       });

       get("/employee/:id", (req, res) -> {
         res.type("application/json");
         String id = req.params(":id");
         String json = empService.getEmployee(id);
         return json;
       });

       post("/employee", "application/json", (req, res) -> {
         res.type("application/json");
         logger.info (req.body());
         Employee emp = new Gson().fromJson(req.body(), Employee.class);
         String json = empService.addEmployee(emp);
         return json;
       });
    }
}
