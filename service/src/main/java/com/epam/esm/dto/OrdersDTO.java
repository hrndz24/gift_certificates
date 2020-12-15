package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

public class OrdersDTO extends RepresentationModel<OrdersDTO> {

    private List<OrderDTO> orders;

    public OrdersDTO() {
    }

    public OrdersDTO(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrdersDTO ordersDTO = (OrdersDTO) o;
        return Objects.equals(orders, ordersDTO.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orders);
    }

    @Override
    public String toString() {
        return "OrdersDTO{" +
                "orders=" + orders +
                '}';
    }
}
