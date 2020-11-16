package com.epam.esm.dao.impl;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.exception.DAOException;
import com.epam.esm.exception.DAOExceptionCode;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.utils.EntityRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private JdbcTemplate jdbcTemplate;
    private OrderResultSetExtractor orderResultSetExtractor = OrderResultSetExtractor.INSTANCE;

    private static final String ORDER_TABLE_NAME = "`order`";

    @Autowired
    public OrderDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order addOrder(Order order) {
        Map<String, Object> parameters = fillInOrderParameters(order);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(ORDER_TABLE_NAME)
                .usingGeneratedKeyColumns(ColumnLabel.ID.getColumnName());
        try {
            order.setId(simpleJdbcInsert.executeAndReturnKey(parameters).intValue());
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_ADD_ORDER.getErrorCode(), e);
        }
        return order;
    }

    private Map<String, Object> fillInOrderParameters(Order order) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ColumnLabel.ORDER_ID.getColumnName(), order.getId());
        parameters.put(ColumnLabel.USER_ID.getColumnName(), order.getUserId());
        parameters.put(ColumnLabel.ORDER_COST.getColumnName(), order.getCost());
        parameters.put(ColumnLabel.ORDER_DATE.getColumnName(), order.getDate());
        return parameters;
    }

    @Override
    public List<Order> getOrders(String queryCondition) {
        try {
            return jdbcTemplate.query(SQLQuery.GET_ORDERS.getQuery() + queryCondition,
                    orderResultSetExtractor);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_ORDERS.getErrorCode(), e);
        }
    }

    @Override
    public Order getOrderById(int id) {
        try {
            return Objects.requireNonNull(jdbcTemplate.query(SQLQuery.GET_ORDER_BY_ID.getQuery(),
                    orderResultSetExtractor, id)).stream().findFirst().orElse(null);
        } catch (DataAccessException e) {
            throw new DAOException(DAOExceptionCode.FAILED_GET_ORDER.getErrorCode(), e);
        }
    }

    private enum OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {
        INSTANCE(new EntityRowMapper());

        private EntityRowMapper entityRowMapper;

        OrderResultSetExtractor(EntityRowMapper entityRowMapper) {
            this.entityRowMapper = entityRowMapper;
        }

        @Override
        public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Order> orders = new ArrayList<>();
            Order currentOrder = null;
            GiftCertificate currentCertificate = null;
            while (rs.next()) {
                int id = rs.getInt(ColumnLabel.ORDER_ID.getColumnName());

                if (currentOrder == null) {
                    currentOrder = mapOrder(rs);
                } else if (currentOrder.getId() != id) {
                    currentOrder.addCertificate(currentCertificate);
                    currentCertificate = null;
                    orders.add(currentOrder);
                    currentOrder = mapOrder(rs);
                }
                int certificateId = rs.getInt(ColumnLabel.ID.getColumnName());
                if (currentCertificate == null) {
                    currentCertificate = mapCertificate(rs);
                } else if (currentCertificate.getId() != certificateId) {
                    currentOrder.addCertificate(currentCertificate);
                    currentCertificate = mapCertificate(rs);
                }
                int tagId = rs.getInt(ColumnLabel.TAG_ID.getColumnName());
                if (tagId != 0) {
                    currentCertificate.addTag(mapTag(rs));
                }
            }
            if (currentOrder != null) {
                if (currentCertificate != null) {
                    currentOrder.addCertificate(currentCertificate);
                }
                orders.add(currentOrder);
            }
            return orders;
        }

        private Order mapOrder(ResultSet rs) throws SQLException {
            return entityRowMapper.mapOrderFields(rs);
        }

        private GiftCertificate mapCertificate(ResultSet rs) throws SQLException {
            return entityRowMapper.mapCertificateFields(rs);
        }

        private Tag mapTag(ResultSet rs) throws SQLException {
            return entityRowMapper.mapTagFields(rs);
        }
    }
}
