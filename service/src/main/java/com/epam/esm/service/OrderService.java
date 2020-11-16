package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderDTO addOrder(OrderDTO order);

    List<OrderDTO> getOrders(Map<String, String> params);

    OrderDTO getOrderById(int id);
}
