package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderMapper {

    private ModelMapper mapper;

    public OrderMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Order toModel(OrderDTO dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    public OrderDTO toDTO(Order model) {
        return Objects.isNull(model) ? null : mapper.map(model, OrderDTO.class);
    }
}
