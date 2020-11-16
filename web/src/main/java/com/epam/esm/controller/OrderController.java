package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller used to add new {@code Order} instances
 * and get existing ones
 */
@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new Order in the database.
     *
     * @param order order to be created
     * @return OrderDTO corresponding to Order that was created
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO order) {
        return orderService.addOrder(order);
    }

    /**
     * Returns list of {@code OrderDTO}
     * that represent all orders in the database.
     * Supports search by user id.
     *
     * @return list of OrderDTOs corresponding to orders in the database
     */
    @GetMapping
    public List<OrderDTO> getAllOrders(@RequestParam Map<String, String> params) {
        return orderService.getOrders(params);
    }

    /**
     * Returns {@code OrderDTO} requested by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id id of the requested order
     * @return OrderDTO with the requested id
     */
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable("id") int id) {
        return orderService.getOrderById(id);
    }
}
