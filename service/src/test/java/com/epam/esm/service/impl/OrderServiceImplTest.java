package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
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
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
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
    private OrderMapper orderMapper;
    @Spy
    private OrderQueryGenerator queryGenerator = new OrderQueryGenerator();

    @BeforeEach
    void setUp() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        orderMapper = new OrderMapper(mapper);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addOrderWithValidOrderShouldAddOrder() {
        Order order = new Order();
        order.setId(4);
        order.setUserId(1);
        order.setCost(new BigDecimal("13.00"));
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1);
        certificate.setPrice(new BigDecimal("13.00"));
        order.addCertificate(certificate);
        when(orderDAO.addOrder(any())).thenReturn(order);
        when(orderDAO.getOrderById(4)).thenReturn(order);
        when(userDAO.getUserById(1)).thenReturn(new User());
        when(certificateDAO.getCertificateById(1)).thenReturn(certificate);
        Map<String, Object> fields = new HashMap<>();
        fields.put("userId", 1);
        List<Integer> certificatesId = new ArrayList<>();
        certificatesId.add(1);
        fields.put("certificatesId", certificatesId);
        OrderDTO returnedOrderDTO = orderService.addOrder(fields);
        returnedOrderDTO.setCost(new BigDecimal("13"));
        assertEquals(4, returnedOrderDTO.getId());
    }

    @Test
    void addOrderWithoutCertificatesShouldThrowException() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("userId", 2);
        assertThrows(ValidatorException.class,
                () -> orderService.addOrder(fields));
    }

    @Test
    void addOrderWithNonExistingUserShouldThrowException() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("userId", 4);
        when(userDAO.getUserById(4)).thenReturn(null);
        assertThrows(ValidatorException.class,
                () -> orderService.addOrder(fields));
    }


    @Test
    void getOrdersShouldReturnListOfThreeOrders() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setCost(new BigDecimal("23.00"));
        orders.add(order);
        orders.add(order);
        orders.add(order);
        when(orderDAO.getOrders(queryGenerator.generateQuery(
                new HashMap<>()), 10, 0)).thenReturn(orders);
        doNothing().when(validator).validatePageNumberIsLessThanElementsCount(anyMap(), anyLong());
        assertEquals(3, orderService.getOrders(new HashMap<>()).getOrders().size());
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