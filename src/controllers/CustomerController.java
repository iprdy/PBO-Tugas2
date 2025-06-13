package controllers;

import api.Request;
import api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import models.Customer;

import java.sql.*;
import java.util.*;

public class CustomerController {

    private Connection conn;

    public CustomerController(Connection conn) {
        this.conn = conn;
    }

    public void getAllCustomers(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:../villa_booking.db")) {
            String sql = "SELECT * FROM customers";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                customers.add(customer);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(customers);
            res.json(json);

        } catch (Exception e) {
            res.error("Failed to fetch customers: " + e.getMessage());
        }
    }
}
