package com.example.ordersystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.ordersystem.dto.CreateOrderRequest;
import com.example.ordersystem.dto.OrderItemRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // CREATE ORDER
    // =========================
    @Test
    void shouldCreateOrder() throws Exception {

        CreateOrderRequest request = new CreateOrderRequest();

        OrderItemRequest item = new OrderItemRequest();
        item.setProductName("Phone");
        item.setQuantity(1);
        item.setPrice(BigDecimal.valueOf(500));

        request.setItems(List.of(item));

        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists());
    }

    // =========================
    // GET ORDER
    // =========================
    @Test
    void shouldGetOrderById() throws Exception {

        String response = mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("""
                        {
                          "items":[{"productName":"Phone","quantity":1,"price":500}]
                        }
                        """))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).path("data").path("id").asLong();

        mockMvc.perform(get("/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id));
    }

    // =========================
    // LIST ORDERS
    // =========================
    @Test
    void shouldListOrders() throws Exception {

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // =========================
    // CANCEL ORDER
    // =========================
    @Test
    void shouldCancelOrder() throws Exception {

        String response = mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("""
                        {
                          "items":[{"productName":"Phone","quantity":1,"price":500}]
                        }
                        """))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).path("data").path("id").asLong();

        mockMvc.perform(delete("/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Cancelled"));
    }

    // =========================
    // UPDATE STATUS
    // =========================
    @Test
    void shouldUpdateOrderStatus() throws Exception {

        String response = mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("""
                        {
                          "items":[{"productName":"Phone","quantity":1,"price":500}]
                        }
                        """))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).path("data").path("id").asLong();

        mockMvc.perform(put("/orders/" + id + "/status?status=PROCESSING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Updated"));
    }
}