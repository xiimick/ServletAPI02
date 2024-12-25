package com.example.servlets;

import com.example.models.Order;
import com.example.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServletTest {

    @InjectMocks
    private OrderServlet orderServlet;  // Ваш сервлет

    @Mock
    private HttpServletRequest request;  // Мок запроса

    @Mock
    private HttpServletResponse response;  // Мок ответа

    @Mock
    private PrintWriter writer;  // Мок для записи ответа

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Инициализация моков
    }

    @Test
    public void testDoGet() throws Exception {
        // Подготовка мока для запроса
        when(request.getParameter("id")).thenReturn("1");

        // Создание фиктивного заказа
        Order order = new Order(1, "2024-12-25", 100.0, Collections.singletonList(new Product(1, "Product 1", 50.0)));

        // Мокаем вызов метода для получения заказа
        when(orderServlet.getOrderById(1)).thenReturn(order);

        // Мокаем вывод ответа
        when(response.getWriter()).thenReturn(writer);

        // Вызываем метод doGet с помощью рефлексии
        orderServlet.doGet(request, response);

        // Проверки
        verify(response).setContentType("application/json");  // Убедимся, что тип контента - JSON
        verify(writer).write(anyString());  // Проверим, что метод write был вызван с JSON строкой
    }

    @Test
    public void testDoPost() throws Exception {
        // Мокируем входной JSON
        String jsonInput = "{ \"id\": 2, \"date\": \"2024-12-26\", \"cost\": 150.0, \"products\": [] }";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));

        // Мокаем вывод ответа
        when(response.getWriter()).thenReturn(writer);

        // Вызываем метод doPost
        orderServlet.doPost(request, response);

        // Проверки
        verify(response).setStatus(HttpServletResponse.SC_CREATED);  // Убедимся, что статус 201 (Created)
        verify(writer).write(anyString());  // Проверим, что write был вызван с каким-то содержимым
    }

    @Test
    public void testDoPut() throws Exception {
        // Мокируем входной JSON для обновления
        String jsonInput = "{ \"id\": 1, \"date\": \"2024-12-25\", \"cost\": 200.0, \"products\": [] }";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));

        // Мокаем вывод ответа
        when(response.getWriter()).thenReturn(writer);

        // Вызываем метод doPut
        orderServlet.doPut(request, response);

        // Проверки
        verify(response).setStatus(HttpServletResponse.SC_OK);  // Убедимся, что статус 200 (OK)
    }

    @Test
    public void testDoDelete() throws Exception {
        // Мокируем параметр запроса для удаления
        when(request.getParameter("id")).thenReturn("1");

        // Мокаем вывод ответа
        when(response.getWriter()).thenReturn(writer);

        // Вызываем метод doDelete
        orderServlet.doDelete(request, response);

        // Проверки
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);  // Убедимся, что статус 204 (No Content)
    }
}