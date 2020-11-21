package com.epam.esm.dao;

import com.epam.esm.model.Order;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public interface OrderDAO {

    Order addOrder(Order order);

    List<Order> getOrders(CriteriaQuery<Order> criteriaQuery, int limit, int offset);

    Order getOrderById(int id);
}
