package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private HateoasBuilder hateoasBuilder;

    @Autowired
    public OrderController(OrderService orderService,
                           HateoasBuilder hateoasBuilder) {
        this.orderService = orderService;
        this.hateoasBuilder = hateoasBuilder;
    }

    /**
     * Creates a new Order in the database.
     *
     * @param fields order fields to create an order from
     * @return OrderDTO corresponding to Order that was created
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public OrderDTO createOrder(@RequestBody Map<String, Object> fields) {
        return orderService.addOrder(fields);
    }

    /**
     * Returns list of {@code OrderDTO}
     * that represent all orders in the database.
     * Supports search by user id.
     *
     * @return list of OrderDTOs corresponding to orders in the database
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or (hasAuthority('USER') and params.get('userId').equals(authentication.principal.id))")
    public RepresentationModel<?> getAllOrders(@RequestParam Map<String, String> params) {
        List<OrderDTO> orders = orderService.getOrders(params);
        long ordersCount = orderService.getCount(params);
        return hateoasBuilder.addLinksForListOfOrderDTOs(orders, params, ordersCount);
    }

    /**
     * Returns {@code OrderDTO} requested by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id id of the requested order
     * @return OrderDTO with the requested id
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('USER')")
    public OrderDTO getOrderById(@PathVariable("id") int id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return hateoasBuilder.addLinksForOrderDTO(orderDTO);
    }
}
