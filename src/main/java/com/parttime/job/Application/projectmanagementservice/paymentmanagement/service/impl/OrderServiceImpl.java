package com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import com.parttime.job.Application.projectmanagementservice.cart.repository.CartRepository;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentMethod;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.mapper.OrderMapper;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository.OrderRepository;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository.PaymentRepository;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderStatsResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.OrderService;
import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.repository.PointRepository;
import com.parttime.job.Application.projectmanagementservice.product.constant.ProductConstant;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.UserVoucher;
import com.parttime.job.Application.projectmanagementservice.voucher.repository.UserVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final PointRepository pointRepository;
    private final UserVoucherRepository userVoucherRepository;

    @Override
    public PagingResponse<OrderResponse> getListOrder(String searchText, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Orders> orderPage = orderRepository.searchAllOrder(searchText, pageRequest);
        if (orderPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, ProductConstant.PRODUCT_NOT_FOUND);
        }
        List<OrderResponse> ordersResponse = orderMapper.toListDTO(orderPage.getContent());
        return new PagingResponse<>(ordersResponse, pagingRequest, orderPage.getTotalElements());
    }

    @Override
    public PagingResponse<OrderResponse> getListOrderByUser(String userId, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Orders> orderPage = orderRepository.searchAllOrderByUserId(userId, pageRequest);
        if (orderPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, ProductConstant.PRODUCT_NOT_FOUND);
        }
        List<OrderResponse> ordersResponse = orderMapper.toListDTO(orderPage.getContent());
        return new PagingResponse<>(ordersResponse, pagingRequest, orderPage.getTotalElements());
    }

    @Override
    public OrderResponse getOrderDetail(String orderId) {
        return orderMapper.toDTO(orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(MessageCodeConstant.M003_NOT_FOUND, "Order not found")));
    }

    @Override
    public OrderStatus checkOrderStatus(String orderId) {
        Optional<Orders> orders = orderRepository.findById(orderId);
        if (orders.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Order not found");
        }

        return orders.get().getOrderStatus();
    }

    @Override
    public OrderStatsResponse getOrderStats() {
        Long totalCompleted = orderRepository.countTotalCompletedOrders();
        Long todayCompleted = orderRepository.countTodayCompletedOrders();

        double increasePercent = 0.0;

        if (totalCompleted != null && totalCompleted > 0) {
            increasePercent = (todayCompleted.doubleValue() / totalCompleted.doubleValue()) * 100.0;
        }

        return new OrderStatsResponse(totalCompleted, increasePercent);
    }

    @Override
    @Transactional
    public OrderResponse updateStatusOrder(String orderId, OrderStatus status) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        if (payment.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Payment not found");
        }
        if (payment.get().getStatus() != PaymentStatus.PENDING || payment.get().getMethod() != PaymentMethod.CASH) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Cannot update status for payment with id: " + payment.get().getId());
        }
        payment.get().setStatus(status == OrderStatus.COMPLETED ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
        paymentRepository.save(payment.get());
        Orders order = null;
        if (status == OrderStatus.COMPLETED) {
            order = payment.get().getOrders();
            order.setOrderStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);

            Optional<Cart> cart = cartRepository.findByUserIdAndIsActiveTrue(order.getUser().getId());
            cart.get().setActive(false);
            cartRepository.save(cart.get());

            if (cart.get().getVoucher() != null) {
                Optional<UserVoucher> userVoucher = userVoucherRepository.findByUserIdAndVoucherId(order.getUser().getId(), cart.get().getVoucher().getId());
                if (userVoucher.isPresent()) {
                    userVoucher.get().setUsed(true);
                    userVoucherRepository.save(userVoucher.get());
                }
            }
            Cart newCart = new Cart();
            newCart.setUser(cart.get().getUser());
            newCart.setActive(true);

            cartRepository.save(newCart);

            Point point = pointRepository.findByUserId(order.getUser().getId());
            if (point == null) {
                throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Point not found for user: " + order.getUser().getId());
            }
            point.setCurrentPoints((int) (payment.get().getAmount() / 1000 + point.getCurrentPoints()));
            pointRepository.save(point);
        }
        return orderMapper.toDTO(order);
    }

}
