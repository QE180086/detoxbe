package com.parttime.job.Application.projectmanagementservice.shippingmanagement.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentMethod;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository.OrderRepository;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository.PaymentRepository;
import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.repository.PointRepository;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.enumration.ShippingEnumration;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.response.AddressInfo;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.response.OrderShippingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GHNServiceImpl implements GHNService {

    @Value("${ghn.api.key}")
    private String apiKey;

    @Value("${ghn.shop.id}")
    private String shopId;

    @Value("${ghn.baseurl}")
    private String baseUrl;

    @Value("${ghn.from.district.id}")
    private int fromDistrictId;

    @Value("${ghn.from.ward.code}")
    private String fromWardCode;

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final ChangeAddressInfo changeAddressInfo;
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;

    @Override
    public String createShippingOrder(Payment payment) {
        String url = baseUrl + "/shipping-order/create";

        Map<String, Object> body = new HashMap<>();
        body.put("payment_type_id", 1);
        body.put("note", payment.getOrders().getAddress());
        body.put("required_note", "KHONGCHOXEMHANG");

        AddressInfo addressInfo = changeAddressInfo.resolveAddress(payment.getOrders().getAddress());
        if (addressInfo == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "AddressInfo not found");
        }

        body.put("to_name", payment.getOrders().getUser().getUsername());
        body.put("to_phone", payment.getOrders().getNumberPhone());
        body.put("to_address", payment.getOrders().getAddress());
        body.put("to_ward_code", addressInfo.getWardCode());
        body.put("to_district_id", addressInfo.getDistrictId());
        body.put("weight", payment.getOrders().getOrderItems().size() * 348);
        body.put("content", payment.getContent());
        body.put("service_id", 53320);
        body.put("service_type_id", 2);

        if (payment.getMethod().equals(PaymentMethod.CASH)) {
            body.put("cod_amount", payment.getAmount() + payment.getOrders().getShippingFee());
        } else {
            body.put("cod_amount", 0);
        }


        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        for (int i = 0; i < payment.getOrders().getOrderItems().size(); i++) {
            item.put("name", payment.getOrders().getOrderItems().get(i).getProduct().getName());
            item.put("code", "ITEM_" + payment.getOrders().getOrderItems().get(i).getProduct().getId());
            item.put("quantity", payment.getOrders().getOrderItems().get(i).getQuantity());
            item.put("price", (int) payment.getOrders().getOrderItems().get(i).getPrice());
            item.put("weight", 348);
            items.add(item);
            item = new HashMap<>();
        }
        body.put("items", items);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", apiKey);
        headers.set("ShopId", shopId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        JSONObject json = new JSONObject(response.getBody());
        if (json.has("data")) {
            JSONObject data = json.getJSONObject("data");
            if (data.has("order_code") && data.has("expected_delivery_time")) {
                String orderCode = data.getString("order_code");
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(data.getString("expected_delivery_time"));
                LocalDateTime expectedDeliveryTime = offsetDateTime.toLocalDateTime();

                System.out.println("Code GHN: " + orderCode);
                System.out.println("expected_delivery_time GHN: " + expectedDeliveryTime);

                orderRepository.updateOrderCodeAndTime(payment.getOrders().getId(), orderCode, expectedDeliveryTime);
            }
        }
        return response.getBody();
    }

    public double calculateShippingFee(int toDistrictId, String toWardCode, int weight) {
        String url = baseUrl + "/shipping-order/fee";

        Map<String, Object> body = new HashMap<>();
        body.put("from_district_id", fromDistrictId);
        body.put("service_type_id", 2);
        body.put("to_district_id", toDistrictId);
        body.put("to_ward_code", toWardCode);
        body.put("weight", weight);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", apiKey);
        headers.set("ShopId", String.valueOf(shopId));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        JSONObject json = new JSONObject(response.getBody());
        JSONObject data = json.getJSONObject("data");
        return data.getDouble("total");
    }


    @Override
    public String getShippingOrderStatus() {

        return "";
    }

    @Override
    public OrderShippingResponse getDetailOrderStatusByOrderCode(String orderCode) {
        String url = baseUrl + "/shipping-order/detail";

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("order_code", orderCode);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Token", apiKey);
            headers.set("ShopId", shopId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            JsonNode dataNode = root.path("data");
            if (dataNode.isArray()) {
                dataNode = dataNode.get(0);
            }

            return mapper.treeToValue(dataNode, OrderShippingResponse.class);

        } catch (Exception e) {
            log.error("error while get order detail: " + e.getMessage());
            e.printStackTrace();
            throw new AppException(MessageCodeConstant.M002_ERROR, "Error while get order detail from GHN.");
        }
    }

    @Transactional
    @Override
    public String cancelOrder(String orderId) {
        Optional<Orders> orders = orderRepository.findById(orderId);
        if (orders.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Order not found");
        }
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        if (payment.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Payment not found for this order");
        }
        boolean canCancel =
                (payment.get().getMethod().equals(PaymentMethod.CASH) && orders.get().getOrderStatus().equals(OrderStatus.PENDING)) ||
                        (payment.get().getMethod().equals(PaymentMethod.BANK) && orders.get().getOrderStatus().equals(OrderStatus.COMPLETED));

        if (!canCancel) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Order cannot be cancelled.");
        }
        if (!orders.get().getOrderStatus().equals(OrderStatus.COMPLETED)) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Only completed orders status can be cancelled");
        }
        if (orders.get().getOrderCode() != null) {
            OrderShippingResponse orderShippingResponse = this.getDetailOrderStatusByOrderCode(orders.get().getOrderCode());
            if (canCancelOrder(ShippingEnumration.fromValue(orderShippingResponse.getStatus()))) {
                String url = baseUrl + "/shipping-order/cancel";

                Map<String, Object> body = new HashMap<>();
                body.put("order_code", orders.get().getOrderCode());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Token", apiKey);
                headers.set("ShopId", shopId);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                JSONObject json = new JSONObject(response.getBody());
                if (json.has("data")) {
                    JSONObject data = json.getJSONObject("data");
                    if (data.has("code") && data.getInt("code") == 200) {
                        log.info("Cancel order success for orderId: " + orderId);
                        orders.get().setOrderStatus(OrderStatus.CANCELLED);
                        orderRepository.save(orders.get());
                        paymentRepository.updatePaymentStatusByOrderId(orderId, PaymentStatus.CANCELLED);
                        if (payment.get().getMethod().equals(PaymentMethod.CASH)) {
                            return "You haved cancelled the order successfully.";
                        } else {
                            Point point = pointRepository.findByUserId(orders.get().getUser().getId());
                            if (point.getCurrentPoints() >= (payment.get().getAmount() + orders.get().getShippingFee()) / 1000) {
                                point.setCurrentPoints((int) (point.getCurrentPoints() - ((payment.get().getAmount() + orders.get().getShippingFee()) / 1000)));
                                return "Please contact with admin to refund your payment. NumberPhone: 0379560889 with your order_code: " + orders.get().getOrderCode();
                            } else {
                                return "Your points are not enough to refund.";
                            }
                        }
                    } else {
                        throw new AppException(MessageCodeConstant.M002_ERROR, "Failed to cancel shipping order in GHN.");
                    }
                }
            } else {
                log.info("Order has no shipping order code, skipping GHN cancellation.");
                throw new AppException(MessageCodeConstant.M005_INVALID, "Order cannot be cancelled at this stage.");
            }
        }
        throw new AppException(MessageCodeConstant.M005_INVALID, "Order haves no shipping order code.");
    }

    @Override
    public String updateOrder(String orderId, String toName, String toPhone, String address) {
        Optional<Orders> orders = orderRepository.findById(orderId);
        if (orders.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Order not found");
        }
        if (orders.get().getOrderCode() != null) {
            OrderShippingResponse orderShippingResponse = this.getDetailOrderStatusByOrderCode(orders.get().getOrderCode());
            if (canCancelOrder(ShippingEnumration.fromValue(orderShippingResponse.getStatus()))) {
                String url = baseUrl + "/shipping-order/update";

                Map<String, Object> body = new HashMap<>();
                body.put("order_code", orders.get().getOrderCode());
                body.put("to_name", toName);
                body.put("to_phone", toPhone);
                body.put("to_address", address);


                AddressInfo addressInfo = changeAddressInfo.resolveAddress(address);
                if (addressInfo == null) {
                    throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "AddressInfo not found");
                }
                body.put("to_ward_code", addressInfo.getWardCode());
                body.put("to_district_id", addressInfo.getDistrictId());
                body.put("note", address);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Token", apiKey);
                headers.set("ShopId", shopId);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                JSONObject json = new JSONObject(response.getBody());
                if (json.has("code") && json.getInt("code") == 200) {
                    log.info("Update order success for orderId: " + orderId + " (No data returned)");
                    return "You have updated the order successfully.";
                } else {
                    throw new AppException(MessageCodeConstant.M002_ERROR, "Failed to update shipping order in GHN.");
                }
            } else {
                throw new AppException(MessageCodeConstant.M005_INVALID, "Order cannot be updated at this stage.");
            }
        }
        throw new AppException(MessageCodeConstant.M002_ERROR, "Error order have no shipping order code.");
    }


    public boolean canCancelOrder(ShippingEnumration status) {
        return status == ShippingEnumration.READY_TO_PICK ||
                status == ShippingEnumration.PICKING ||
                status == ShippingEnumration.MONEY_COLLECT_PICKING;
    }
}
