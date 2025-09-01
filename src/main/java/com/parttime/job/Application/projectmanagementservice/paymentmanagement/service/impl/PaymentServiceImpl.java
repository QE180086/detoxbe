package com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import com.parttime.job.Application.projectmanagementservice.cart.repository.CartRepository;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.OrderItem;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentMethod;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.mapper.PaymentMapper;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository.OrderRepository;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository.PaymentRepository;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.request.PaymentRequest;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.request.SepayWebhookRequest;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.PaymentResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.PaymentService;
import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.repository.PointRepository;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Address;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Information;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import com.parttime.job.Application.projectmanagementservice.profile.repository.AddressRepository;
import com.parttime.job.Application.projectmanagementservice.profile.repository.InformationRepository;
import com.parttime.job.Application.projectmanagementservice.profile.repository.ProfileRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.UserVoucher;
import com.parttime.job.Application.projectmanagementservice.voucher.repository.UserVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RANDOM_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserUtilService userUtilService;
    private final CartRepository cartRepository;
    private final PaymentMapper paymentMapper;
    private final VietQRService vietQRService;
    private final UserVoucherRepository userVoucherRepository;
    private final PointRepository pointRepository;
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final InformationRepository informationRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        if (!checkOrderPending()) {
            createOrder();
        }
        Optional<Orders> orders = orderRepository.findFirstByUserIdAndOrderStatusOrderByCreatedDateDesc(userUtilService.getIdCurrentUser(), OrderStatus.PENDING);
        Payment payment = new Payment();
        payment.setAmount(orders.get().getTotalAmount());
        payment.setMethod(paymentRequest.getMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setOrders(orders.get());
        payment.setContent(generateContent());

        if (paymentRequest.getMethod() == PaymentMethod.BANK) {
            payment.setQrCode(vietQRService.generateFixedQRUrl(payment.getContent(), payment.getAmount()));
        }
        paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    @Transactional
    public PaymentResponse handleWebhook(SepayWebhookRequest request) {
        System.out.println("Webhook received: " + request);
// Set order
        Orders order = orderRepository.findFirstByUserIdAndOrderStatusOrderByCreatedDateDesc(userUtilService.getIdCurrentUser(), OrderStatus.PENDING)
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Order not found"));

        if (!(order.getTotalAmount() == request.getAmount())) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Amount mismatch for order: " + order.getId());
        }
        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        System.out.println("Order " + order.getId() + " marked as COMPLETED.");
// Set payment
        Optional<Payment> payment = paymentRepository.findByOrderId(order.getId());
        if (payment.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Payment not found for order: " + order.getId());
        }

        payment.get().setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment.get());
        System.out.println("Payment for order " + order.getId() + " marked as COMPLETED.");
// Set cart
        Optional<Cart> cart = cartRepository.findByUserIdAndIsActiveTrue(userUtilService.getIdCurrentUser());
        cart.get().setActive(false);
        cartRepository.save(cart.get());

        if (cart.get().getVoucher() != null) {
            Optional<UserVoucher> userVoucher = userVoucherRepository.findByUserIdAndVoucherId(userUtilService.getIdCurrentUser(), cart.get().getVoucher().getId());
            if (userVoucher.isPresent()) {
                userVoucher.get().setUsed(true);
                userVoucherRepository.save(userVoucher.get());
            }
        }
        Cart newCart = new Cart();
        newCart.setUser(userUtilService.getCurrentUser());
        newCart.setActive(true);

        cartRepository.save(newCart);
// Set point
        Point point = pointRepository.findByUserId(userUtilService.getIdCurrentUser());
        if (point == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Point not found for user: " + userUtilService.getIdCurrentUser());
        }
        point.setCurrentPoints((int) (request.getAmount() / 1000 + point.getCurrentPoints()));
        pointRepository.save(point);
        return null;
    }


    private boolean checkOrderPending() {
        if (orderRepository.existsPendingOrderByUserId(userUtilService.getIdCurrentUser())) {
            return true;
        }
        return false;
    }

    private Orders createOrder() {
        Optional<Cart> cart = cartRepository.findByUserIdAndIsActiveTrue(userUtilService.getIdCurrentUser());
        if (cart.isEmpty() || cart.get().getCartItems().isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart is empty");
        }
        Optional<Address> address = addressRepository.findDefaultAddressByUserId(userUtilService.getIdCurrentUser());
        if (address.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Update address in profile before place order");
        }
        Optional<Profile> profile = profileRepository.findByUserId(userUtilService.getIdCurrentUser());
        if (profile.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Profile not found");
        }
        if (!StringUtils.hasText(profile.get().getPhoneNumber())) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Update number phone in profile before place order");
        }
        Orders orders = new Orders();
        orders.setUser(userUtilService.getCurrentUser());
        orders.setOrderStatus(OrderStatus.PENDING);
        orders.setTotalAmount(cart.get().getDiscountedPrice());
        orders.setOrderItems(createListOrderItem(orders));
        orders.setAddress(address.get().getAddress());
        orders.setNumberPhone(profile.get().getPhoneNumber());
        return orderRepository.save(orders);
    }

    private List<OrderItem> createListOrderItem(Orders orders) {
        Optional<Cart> cart = cartRepository.findByUserIdAndIsActiveTrue(userUtilService.getIdCurrentUser());
        if (cart.isEmpty() || cart.get().getCartItems().isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart is empty");
        }
        return cart.get().getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setOrders(orders);
            return orderItem;
        }).toList();
    }


    public static String generateContent() {
        StringBuilder sb = new StringBuilder("DC24");
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
