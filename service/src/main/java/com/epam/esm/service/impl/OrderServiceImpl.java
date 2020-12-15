package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrdersDTO;
import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.exception.ValidatorException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.model.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.specification.SearchConditionSpecification;
import com.epam.esm.utils.OrderQueryGenerator;
import com.epam.esm.utils.ServiceConstant;
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
@Transactional
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
    public OrderDTO addOrder(Map<String, Object> fields) {
        validator.validateOrderFields(fields);
        OrderDTO order = buildOrderFromFields(fields);
        prepareOrderBeforeAddingToDatabase(order);
        return orderMapper.toDTO(orderDAO.addOrder(orderMapper.toModel(order)));
    }

    @SuppressWarnings("unchecked cast")
    private OrderDTO buildOrderFromFields(Map<String, Object> fields) {
        OrderDTO order = new OrderDTO();
        order.setUserId((Integer) fields.get(ServiceConstant.USER_ID_PARAM.getValue()));
        List<Integer> certificatesIdValues = (ArrayList<Integer>) fields.get(ServiceConstant.CERTIFICATES_ID_FIELD.getValue());
        certificatesIdValues.forEach(certificateId -> {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            certificate.setId(certificateId);
            order.getCertificates().add(certificate);
        });
        return order;
    }

    private void prepareOrderBeforeAddingToDatabase(OrderDTO order) {
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
    public OrdersDTO getOrders(Map<String, String> params) {
        validator.validateOrderParams(params);
        checkOrderParamsValuesExist(params);
        return new OrdersDTO(getOrdersFromDatabase(params));
    }

    @Override
    public long getCount(Map<String, String> params) {
        return orderDAO.getCount(queryGenerator.generateQuery(params));
    }

    private List<OrderDTO> getOrdersFromDatabase(Map<String, String> params) {
        List<OrderDTO> orders = new ArrayList<>();
        int limit = Integer.parseInt(params.get(ServiceConstant.SIZE_PARAM.getValue()));
        int offset = (Integer.parseInt(params.get(ServiceConstant.PAGE_PARAM.getValue())) - 1) * limit;
        List<SearchConditionSpecification> specifications = queryGenerator.generateQuery(params);
        long elementsCount = orderDAO.getCount(specifications);
        validator.validatePageNumberIsLessThanElementsCount(params, elementsCount);
        orderDAO.getOrders(specifications, limit, offset)
                .forEach(order -> orders.add(orderMapper.toDTO(order)));
        return orders;
    }

    private void checkOrderParamsValuesExist(Map<String, String> params) {
        if (!params.isEmpty() && NumberUtils.isParsable(params.get(ServiceConstant.USER_ID_PARAM.getValue()))) {
            checkUserExists(Integer.parseInt(params.get(ServiceConstant.USER_ID_PARAM.getValue())));
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
