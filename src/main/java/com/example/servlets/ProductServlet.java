package com.example.servlets;

import com.example.models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductServlet extends HttpServlet {
    private static List<Product> products = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Создание нового продукта
        Product product = objectMapper.readValue(request.getReader(), Product.class);
        products.add(product);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("Product created successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получение продукта по ID
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);

        if (product != null) {
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(product));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Product not found");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Обновление продукта
        int productId = Integer.parseInt(request.getParameter("id"));
        Product existingProduct = products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);

        if (existingProduct != null) {
            Product updatedProduct = objectMapper.readValue(request.getReader(), Product.class);
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setCost(updatedProduct.getCost());
            response.getWriter().write("Product updated successfully");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Product not found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Удаление продукта
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);

        if (product != null) {
            products.remove(product);
            response.getWriter().write("Product deleted successfully");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Product not found");
        }
    }
}