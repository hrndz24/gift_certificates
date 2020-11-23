package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.utils.OrderQueryGenerator;
import com.epam.esm.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private GiftCertificateDAO certificateDAO;
    @Mock
    private UserDAO userDAO;
    @Spy
    private Validator validator = new Validator();
    @Spy
    private OrderMapper orderMapper = new OrderMapper(new ModelMapper());
    @Mock
    private OrderQueryGenerator queryGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addOrderWithValidOrderShouldAddOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1);
        orderDTO.setCost(new BigDecimal("123"));
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setId(1);
        certificateDTO.setPrice(new BigDecimal("13.00"));
        orderDTO.getCertificates().add(certificateDTO);
        Order order = new Order();
        order.setId(4);
        order.setUserId(1);
        order.setCost(new BigDecimal("13.00"));
        when(orderDAO.addOrder(any())).thenReturn(order);
        when(orderDAO.getOrderById(4)).thenReturn(order);
        when(userDAO.getUserById(1)).thenReturn(new User());
        GiftCertificate certificate = new GiftCertificate();
        certificate.setPrice(new BigDecimal("13.00"));
        when(certificateDAO.getCertificateById(1)).thenReturn(certificate);
        OrderDTO returnedOrderDTO = orderService.addOrder(orderDTO);
        returnedOrderDTO.setCost(new BigDecimal("123"));
        assertEquals(4, returnedOrderDTO.getId());
        assertNotNull(orderDTO.getDate());
        assertNotNull(orderDTO.getCost());
    }

    @Test
    void addOrderWithoutCertificatesShouldThrowException() {
        OrderDTO order = new OrderDTO();
        assertThrows(ValidatorException.class,
                () -> orderService.addOrder(order));
    }

    @Test
    void addOrderWithNonExistingUserShouldThrowException() {
        OrderDTO order = new OrderDTO();
        order.setUserId(4);
        when(userDAO.getUserById(4)).thenReturn(null);
        assertThrows(ValidatorException.class,
                () -> orderService.addOrder(order));
    }


    @Test
    void getOrdersShouldReturnListOfThreeOrders() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setCost(new BigDecimal("23.00"));
        orders.add(order);
        orders.add(order);
        orders.add(order);
        when(queryGenerator.generateQuery(new HashMap<>())).thenReturn(any());
        when(orderDAO.getOrders(queryGenerator.generateQuery(
                new HashMap<>()), 10, 0)).thenReturn(orders);
        assertEquals(3, orderService.getOrders(new HashMap<>()).size());
    }

    @Test
    void getOrdersByUserIdWithNonExistingUserShouldThrowException() {
        when(userDAO.getUserById(4)).thenReturn(null);
        Map<String, String> params = new HashMap<>();
        params.put("userId", "4");
        assertThrows(ValidatorException.class,
                () -> orderService.getOrders(params));
    }

    @Test
    void getOrderByIdWithNonExistingIdShouldThrowException() {
        when(orderDAO.getOrderById(4)).thenReturn(null);
        assertThrows(ValidatorException.class,
                () -> orderService.getOrderById(4));
    }
}