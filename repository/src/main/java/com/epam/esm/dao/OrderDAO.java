package com.epam.esm.dao;

import com.epam.esm.model.Order;

import java.util.List;

public interface OrderDAO {

    Order addOrder(Order order);

    List<Order> getOrders(String queryCondition);

    Order getOrderById(int id);
}
