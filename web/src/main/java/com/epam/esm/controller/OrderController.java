package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.ControllerPaginationPreparer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller used to add new {@code Order} instances
 * and get existing ones
 */
@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private OrderService orderService;
    private ControllerPaginationPreparer paginationPreparer;

    @Autowired
    public OrderController(OrderService orderService,
                           ControllerPaginationPreparer paginationPreparer) {
        this.orderService = orderService;
        this.paginationPreparer = paginationPreparer;
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
    public RepresentationModel<?> getAllOrders(@RequestParam Map<String, String> params) {
        List<OrderDTO> orders = orderService.getOrders(params);
        orders.forEach(order -> {
            order.add(linkTo(methodOn(OrderController.class)
                    .getOrderById(order.getId()))
                    .withSelfRel());
        });
        int currentPage = Integer.parseInt(params.get("page"));
        long ordersCount = orderService.getCount(params);
        List<Link> links = paginationPreparer.prepareLinks(
                methodOn(OrderController.class).getAllOrders(params), params, currentPage, ordersCount);
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, currentPage, ordersCount);
        CollectionModel<OrderDTO> collectionModel = CollectionModel.of(orders);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(page, LinkRelation.of("page")).build();
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
        OrderDTO orderDTO = orderService.getOrderById(id);
        orderDTO.getCertificates().forEach(certificate -> {
            certificate.add(linkTo(methodOn(GiftCertificateController.class)
                    .getCertificateById(certificate.getId()))
                    .withSelfRel());
        });
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(orderDTO.getUserId()));
        orderDTO.add(linkTo(methodOn(OrderController.class)
                .getAllOrders(params))
                .withRel("all orders of this user"));
        return orderDTO;
    }
}
