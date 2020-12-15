package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrdersDTO;

import java.util.Map;

public interface OrderService {

    OrderDTO addOrder(Map<String, Object> fields);

    OrdersDTO getOrders(Map<String, String> params);

    OrderDTO getOrderById(int id);

    long getCount(Map<String, String> params);
}
