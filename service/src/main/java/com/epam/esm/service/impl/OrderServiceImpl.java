package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.model.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.utils.OrderQueryGenerator;
import com.epam.esm.validation.Validator;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDAO orderDAO;
    private GiftCertificateDAO certificateDAO;
    private UserDAO userDAO;
    private Validator validator;
    private OrderMapper orderMapper;
    private OrderQueryGenerator queryGenerator;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO,
                            GiftCertificateDAO certificateDAO,
                            UserDAO userDAO,
                            Validator validator,
                            OrderMapper orderMapper,
                            OrderQueryGenerator queryGenerator) {
        this.orderDAO = orderDAO;
        this.certificateDAO = certificateDAO;
        this.userDAO = userDAO;
        this.validator = validator;
        this.orderMapper = orderMapper;
        this.queryGenerator = queryGenerator;
    }

    @Override
    @Transactional
    public OrderDTO addOrder(OrderDTO order) {
        prepareOrderBeforeAddingToDatabase(order);
        return orderMapper.toDTO(orderDAO.addOrder(orderMapper.toModel(order)));
    }

    private void prepareOrderBeforeAddingToDatabase(OrderDTO order) {
        validator.validateOrder(order);
        checkUserExists(order.getUserId());
        BigDecimal cost = calculateOrderCost(order.getCertificates());
        order.setCost(cost);
        order.setDate(new Date());
    }

    private BigDecimal calculateOrderCost(List<GiftCertificateDTO> certificates) {
        BigDecimal cost = new BigDecimal(0);
        for (GiftCertificateDTO certificate : certificates) {
            validator.validateCertificateForOrdering(certificate);
            checkCertificateIsExistent(certificate.getId());
            cost = cost.add(certificateDAO.getCertificateById(certificate.getId()).getPrice());
        }
        return cost;
    }

    private void checkCertificateIsExistent(int certificateId) {
        if (certificateDAO.getCertificateById(certificateId) == null) {
            throw new ValidatorException(
                    ServiceExceptionCode.NON_EXISTING_CERTIFICATE_ID.getErrorCode(), String.valueOf(certificateId));
        }
    }

    private void checkUserExists(int userId) {
        if (userDAO.getUserById(userId) == null) {
            throw new ValidatorException(
                    ServiceExceptionCode.NON_EXISTING_USER_ID.getErrorCode(), String.valueOf(userId));
        }
    }

    @Override
    public List<OrderDTO> getOrders(Map<String, String> params) {
        validator.validateOrderParams(params);
        checkOrderParamsValuesExist(params);
        return getOrdersFromDatabase(params);
    }

    private List<OrderDTO> getOrdersFromDatabase(Map<String, String> params) {
        List<OrderDTO> orders = new ArrayList<>();
        int limit = Integer.parseInt(params.get("size"));
        int offset = (Integer.parseInt(params.get("page")) - 1) * limit;
        orderDAO.getOrders(queryGenerator.generateQuery(params), limit, offset).forEach(order -> {
            orders.add(orderMapper.toDTO(order));
        });
        return orders;
    }

    private void checkOrderParamsValuesExist(Map<String, String> params) {
        if (!params.isEmpty() && NumberUtils.isParsable(params.get("userId"))) {
            checkUserExists(Integer.parseInt(params.get("userId")));
        }
    }

    @Override
    public OrderDTO getOrderById(int id) {
        validator.validateIdIsPositive(id);
        return orderMapper.toDTO(getOrderIfExists(id));
    }

    private Order getOrderIfExists(int id) {
        Order order = orderDAO.getOrderById(id);
        if (order == null) {
            throw new ValidatorException(
                    ServiceExceptionCode.NON_EXISTING_ORDER_ID.getErrorCode(), String.valueOf(id));
        }
        return order;
    }
}
