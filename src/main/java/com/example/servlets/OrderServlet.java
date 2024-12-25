package com.example.servlets;

import com.example.models.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderServlet extends HttpServlet {
    public static List<Order> orders = new ArrayList<>();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(request.getReader(), Order.class);
        orders.add(order);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("Order created successfully");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        for (Order order : orders) {
            if (String.valueOf(order.getId()).equals(id)) {
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(order));
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("Order not found");
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        for (Order order : orders) {
            if (String.valueOf(order.getId()).equals(id)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Order updatedOrder = objectMapper.readValue(request.getReader(), Order.class);
                orders.remove(order);
                orders.add(updatedOrder);
                response.getWriter().write("Order updated successfully");
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("Order not found");
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        orders.removeIf(order -> String.valueOf(order.getId()).equals(id));
        response.getWriter().write("Order deleted successfully");
    }
    // Метод получения заказа по id
    public Order getOrderById(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Преобразование объекта Order в JSON
    private String orderToJson(Order order) {
        // Реализация преобразования в JSON
        return "{\"id\": " + order.getId() + ", \"date\": \"" + order.getDate() + "\", \"cost\": " + order.getCost() + "}";
    }

}
