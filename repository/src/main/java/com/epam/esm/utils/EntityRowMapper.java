package com.epam.esm.utils;

import com.epam.esm.dao.ColumnLabel;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EntityRowMapper {

    public Tag mapTagFields(ResultSet rs) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getInt(ColumnLabel.TAG_ID.getColumnName()));
        tag.setName(rs.getString(ColumnLabel.TAG_NAME.getColumnName()));
        return tag;
    }

    public GiftCertificate mapCertificateFields(ResultSet rs) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getInt(ColumnLabel.ID.getColumnName()));
        giftCertificate.setName(rs.getString(ColumnLabel.CERTIFICATE_NAME.getColumnName()));
        giftCertificate.setDescription(rs.getString(ColumnLabel.CERTIFICATE_DESCRIPTION.getColumnName()));
        giftCertificate.setPrice(rs.getBigDecimal(ColumnLabel.CERTIFICATE_PRICE.getColumnName()));
        giftCertificate.setCreateDate(rs.getTimestamp(ColumnLabel.CERTIFICATE_CREATE_DATE.getColumnName()));
        giftCertificate.setLastUpdateDate(rs.getTimestamp(ColumnLabel.CERTIFICATE_LAST_UPDATE_DATE.getColumnName()));
        giftCertificate.setDuration(rs.getInt(ColumnLabel.CERTIFICATE_DURATION.getColumnName()));
        return giftCertificate;
    }

    public User mapUserFields(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(ColumnLabel.ID.getColumnName()));
        user.setEmail(rs.getString(ColumnLabel.USER_EMAIL.getColumnName()));
        user.setPassword(rs.getString(ColumnLabel.USER_PASSWORD.getColumnName()));
        return user;
    }
}
