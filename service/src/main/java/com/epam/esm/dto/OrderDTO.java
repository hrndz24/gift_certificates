package com.epam.esm.dto;

import com.epam.esm.utils.StringConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OrderDTO extends RepresentationModel<OrderDTO> {

    private int id;
    private int userId;
    @JsonFormat(pattern = StringConstant.DATE_FORMAT)
    private Date date;
    private BigDecimal cost;
    private Set<GiftCertificateDTO> certificates = new HashSet<>();

    public OrderDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost.setScale(2, RoundingMode.HALF_UP);
    }

    public Set<GiftCertificateDTO> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<GiftCertificateDTO> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id == orderDTO.id &&
                userId == orderDTO.userId &&
                Objects.equals(date, orderDTO.date) &&
                Objects.equals(cost, orderDTO.cost) &&
                certificates.equals(orderDTO.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", cost=" + cost +
                ", certificates=" + certificates +
                '}';
    }
}
