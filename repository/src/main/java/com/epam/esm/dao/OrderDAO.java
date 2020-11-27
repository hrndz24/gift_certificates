package com.epam.esm.dao;

import com.epam.esm.model.Order;
import com.epam.esm.specification.SearchConditionSpecification;

import java.util.List;

public interface OrderDAO {

    Order addOrder(Order order);

    List<Order> getOrders(List<SearchConditionSpecification> specifications, int limit, int offset);

    Order getOrderById(int id);

    long getCount(List<SearchConditionSpecification> specifications);
}
