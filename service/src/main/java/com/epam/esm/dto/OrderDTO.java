package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderDTO {

    private int id;
    private int userId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private Date date;
    private BigDecimal cost;
    private List<GiftCertificateDTO> certificates = new ArrayList<>();

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
        this.cost = cost;
    }

    public List<GiftCertificateDTO> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<GiftCertificateDTO> certificates) {
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