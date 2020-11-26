package com.epam.esm.util;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.utils.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateoasBuilder {

    private ControllerPaginationPreparer paginationPreparer;

    @Autowired
    public HateoasBuilder(ControllerPaginationPreparer paginationPreparer) {
        this.paginationPreparer = paginationPreparer;
    }

    public OrderDTO addLinksForOrderDTO(OrderDTO orderDTO) {
        orderDTO.getCertificates().forEach(certificate -> certificate.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificateById(certificate.getId()))
                .withSelfRel()));
        Map<String, String> params = new HashMap<>();
        params.put(ServiceConstant.USER_ID_PARAM.getValue(), String.valueOf(orderDTO.getUserId()));
        orderDTO.add(linkTo(methodOn(OrderController.class)
                .getAllOrders(params))
                .withRel(ControllerConstant.ALL_USER_ORDERS.getValue()));
        return orderDTO;
    }

    public RepresentationModel<?> addLinksForListOfOrderDTOs(List<OrderDTO> orders, Map<String, String> params, long ordersCount) {
        orders.forEach(order -> order.add(linkTo(methodOn(OrderController.class)
                .getOrderById(order.getId()))
                .withSelfRel()));
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, ordersCount);
        List<Link> links = paginationPreparer.preparePaginationLinks(
                methodOn(OrderController.class).getAllOrders(params), params, ordersCount);
        CollectionModel<OrderDTO> collectionModel = CollectionModel.of(orders);
        return buildModel(collectionModel, links, page);
    }

    private RepresentationModel<?> buildModel(Object entity, Iterable<Link> links, Object embeddedEntity) {
        return HalModelBuilder
                .halModelOf(entity)
                .links(links)
                .embed(embeddedEntity, LinkRelation.of(ServiceConstant.PAGE_PARAM.getValue()))
                .build();
    }

    public UserDTO addLinksForUserDTO(UserDTO userDTO) {
        userDTO.add(linkTo(methodOn(UserController.class)
                .getUserById(userDTO.getId()))
                .withSelfRel());
        Map<String, String> params = new HashMap<>();
        params.put(ServiceConstant.USER_ID_PARAM.getValue(), String.valueOf(userDTO.getId()));
        userDTO.add(linkTo(methodOn(OrderController.class)
                .getAllOrders(params))
                .withRel(ControllerConstant.ORDERS_RELATION.getValue()));
        return userDTO;
    }

    public RepresentationModel<?> addLinksForListOfUserDTOs(List<UserDTO> users, Map<String, String> params, long usersCount) {
        users.forEach(user -> user.add(linkTo(methodOn(UserController.class)
                .getUserById(user.getId()))
                .withSelfRel()));
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, usersCount);
        List<Link> links = paginationPreparer.preparePaginationLinks(
                methodOn(UserController.class).getUsers(params), params, usersCount);
        CollectionModel<UserDTO> collectionModel = CollectionModel.of(users);
        return buildModel(collectionModel, links, page);
    }

    public TagDTO addLinksForTagDTO(TagDTO tagDTO) {
        tagDTO.add(linkTo(methodOn(TagController.class)
                .getTagById(tagDTO.getId()))
                .withSelfRel());
        tagDTO.add(linkTo(methodOn(TagController.class)
                .deleteTag(tagDTO.getId()))
                .withRel(ControllerConstant.DELETE_RELATION.getValue()));
        tagDTO.add(createLinkToGetCertificates(ServiceConstant.TAG_NAME_PARAM.getValue(),
                tagDTO.getName(), ControllerConstant.CERTIFICATES_RELATION.getValue()));
        return tagDTO;
    }

    public RepresentationModel<?> addLinksForListOfTagDTOs(List<TagDTO> tags, Map<String, String> params, long tagsCount) {
        tags.forEach(tagDTO -> tagDTO.add(linkTo(methodOn(TagController.class)
                .getTagById(tagDTO.getId()))
                .withSelfRel()));
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, tagsCount);
        List<Link> links = paginationPreparer.preparePaginationLinks(
                methodOn(TagController.class).getAllTags(params), params, tagsCount);
        CollectionModel<TagDTO> collectionModel = CollectionModel.of(tags);
        return buildModel(collectionModel, links, page);
    }

    public RepresentationModel<?> addLinksForListOfCertificateDTOs(List<GiftCertificateDTO> certificates, Map<String, String> params, long certificatesCount) {
        certificates.forEach(certificate -> certificate.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificateById(certificate.getId()))
                .withSelfRel()));
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, certificatesCount);
        List<Link> links = paginationPreparer.preparePaginationLinks(
                methodOn(GiftCertificateController.class).getCertificates(params),
                params, certificatesCount);
        links.add(createLinkToGetCertificates(ServiceConstant.ORDER_BY_PARAM.getValue(),
                ServiceConstant.SORT_BY_NAME_ASC.getValue(), ControllerConstant.SORT_BY_NAME_ASC_RELATION.getValue()));
        links.add(createLinkToGetCertificates(ServiceConstant.ORDER_BY_PARAM.getValue(),
                ServiceConstant.SORT_BY_NAME_DESC.getValue(), ControllerConstant.SORT_BY_NAME_DESC_RELATION.getValue()));
        links.add(createLinkToGetCertificates(ServiceConstant.ORDER_BY_PARAM.getValue(),
                ServiceConstant.SORT_BY_DATE_ASC.getValue(), ControllerConstant.SORT_BY_DATE_ASC_RELATION.getValue()));
        links.add(createLinkToGetCertificates(ServiceConstant.ORDER_BY_PARAM.getValue(),
                ServiceConstant.SORT_BY_DATE_DESC.getValue(), ControllerConstant.SORT_BY_DATE_DESC_RELATION.getValue()));
        links.add(createLinkToGetCertificates(ServiceConstant.CERTIFICATE_NAME_PARAM.getValue(),
                ControllerConstant.CERTIFICATE_NAME_EXAMPLE.getValue(), ControllerConstant.SEARCH_BY_NAME.getValue()));
        links.add(createLinkToGetCertificates(ServiceConstant.CERTIFICATE_DESCRIPTION_PARAM.getValue(),
                ControllerConstant.CERTIFICATE_DESCRIPTION_EXAMPLE.getValue(), ControllerConstant.SEARCH_BY_DESCRIPTION.getValue()));
        CollectionModel<GiftCertificateDTO> collectionModel = CollectionModel.of(certificates);
        return buildModel(collectionModel, links, page);
    }

    private Link createLinkToGetCertificates(String param, String value, String rel) {
        Map<String, String> params = new HashMap<>();
        params.put(param, value);
        return linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(params))
                .withRel(rel);
    }

    public GiftCertificateDTO addLinksForCertificateDTO(GiftCertificateDTO certificateDTO) {
        certificateDTO.getTags().forEach(tag -> tag.add(linkTo(methodOn(TagController.class)
                .getTagById(tag.getId()))
                .withSelfRel()));
        certificateDTO.add(linkTo(methodOn(GiftCertificateController.class)
                .deleteCertificate(certificateDTO.getId()))
                .withRel(ControllerConstant.DELETE_RELATION.getValue()));
        certificateDTO.add(linkTo(methodOn(GiftCertificateController.class)
                .updateCertificate(certificateDTO.getId(), certificateDTO))
                .withRel(ControllerConstant.UPDATE_RELATION.getValue()));
        return certificateDTO;
    }
}
