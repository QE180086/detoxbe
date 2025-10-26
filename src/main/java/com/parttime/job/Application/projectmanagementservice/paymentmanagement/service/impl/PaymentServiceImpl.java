package com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
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
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.PaymentResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.PaymentService;
import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.repository.PointRepository;
import com.parttime.job.Application.projectmanagementservice.product.constant.ProductConstant;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Address;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Information;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import com.parttime.job.Application.projectmanagementservice.profile.repository.AddressRepository;
import com.parttime.job.Application.projectmanagementservice.profile.repository.InformationRepository;
import com.parttime.job.Application.projectmanagementservice.profile.repository.ProfileRepository;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.response.AddressInfo;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.service.ChangeAddressInfo;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.service.GHNService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.UserVoucher;
import com.parttime.job.Application.projectmanagementservice.voucher.repository.UserVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

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
    private final TaskScheduler taskScheduler;
    private final UserRepository userRepository;
    private final GHNService ghnService;
    private final ChangeAddressInfo changeAddressInfo;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        Optional<Orders> order = orderRepository.findFirstByUserIdAndOrderStatusOrderByCreatedDateDesc(userUtilService.getIdCurrentUser(), OrderStatus.PENDING);
        if(order.isPresent()){
            if (paymentRepository.findByOrderId(order.get().getId()).get().getStatus().equals(PaymentMethod.BANK)) {
                if (order.isPresent()) {
                    order.get().setOrderStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order.get());
                    Optional<Payment> payment = paymentRepository.findByOrderId(order.get().getId());
                    payment.get().setStatus(PaymentStatus.FAILED);
                    paymentRepository.save(payment.get());
                }
            }
        }
        
        Orders orders = createOrder();

        Payment payment = new Payment();
        payment.setAmount(orders.getTotalAmount() + orders.getShippingFee());
        payment.setMethod(paymentRequest.getMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setOrders(orders);
        payment.setContent(generateContent());

        if (paymentRequest.getMethod() == PaymentMethod.BANK) {
            payment.setQrCode(vietQRService.generateFixedQRUrl(payment.getContent(), payment.getAmount()));
        }
        paymentRepository.save(payment);
        if (payment.getMethod() == PaymentMethod.BANK) {
            taskScheduler.schedule(() -> {
                Optional<Payment> opt = paymentRepository.findById(payment.getId());
                if (opt.isPresent()) {
                    Payment p = opt.get();
                    if (p.getStatus() == PaymentStatus.PENDING) {
                        p.setStatus(PaymentStatus.FAILED);
                        paymentRepository.save(p);
                        Orders o = p.getOrders();
                        o.setOrderStatus(OrderStatus.CANCELLED);
                        orderRepository.save(o);
                        System.out.println("Payment with id " + p.getId() + " has been marked as FAILED due to timeout.");
                    }
                }
            }, Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)));
        }

        PaymentResponse response = paymentMapper.toDTO(payment);
        response.setShippingFee(orders.getShippingFee());
        response.setAmountNotFee(orders.getTotalAmount());
        response.setOrderCode(orders.getOrderCode());

        // Call ghn for CASH
        if (paymentRequest.getMethod() == PaymentMethod.CASH) {
            ghnService.createShippingOrder(payment);
        }
        return response;
    }

    @Override
    @Transactional
    public PaymentResponse handleWebhook(SepayWebhookRequest request) {
        System.out.println("Webhook received: " + request);

// Set payment
        Payment payment = paymentRepository.findByContent(extractCode(request.getDescription()));
        if (payment == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Payment not found with content: " + request.getDescription());
        }

        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
        System.out.println("Payment for content " + request.getDescription() + " marked as COMPLETED.");

// Set order
        Orders order = payment.getOrders();

        if (order.getTotalAmount() < request.getTransferAmount()) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Amount mismatch for order: " + order.getId());
        }
        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        System.out.println("Order " + order.getId() + " marked as COMPLETED.");
// Set cart
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
// Set point
        Point point = pointRepository.findByUserId(order.getUser().getId());
        if (point == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Point not found for user: " + order.getUser().getId());
        }
        point.setCurrentPoints((int) (request.getTransferAmount() / 1000 + point.getCurrentPoints()));
        pointRepository.save(point);

// Call shipping
        ghnService.createShippingOrder(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    public PaymentResponse updateStatusForCashPayment(String paymentId, PaymentStatus paymentStatus) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (payment.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Payment not found with id: " + paymentId);
        }
        if (payment.get().getStatus() != PaymentStatus.PENDING || payment.get().getMethod() != PaymentMethod.CASH) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Cannot update status for payment with id: " + paymentId);
        }
        payment.get().setStatus(paymentStatus);
        paymentRepository.save(payment.get());
        if (paymentStatus == PaymentStatus.COMPLETED) {
            Orders order = payment.get().getOrders();
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
        return paymentMapper.toDTO(payment.get());
    }

    // list order
    @Override
    public PagingResponse<PaymentResponse> getListPayment(String userId, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Payment> paymentPage = paymentRepository.searchAllPayment(userId, pageRequest);
        if (paymentPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, ProductConstant.PRODUCT_NOT_FOUND);
        }
        List<PaymentResponse> paymentResponse = paymentMapper.toListDTO(paymentPage.getContent());
        return new PagingResponse<>(paymentResponse, pagingRequest, paymentPage.getTotalElements());
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
        // Shipping fee
        AddressInfo addressInfo = changeAddressInfo.resolveAddress(address.get().getAddress());
        if (addressInfo == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "AddressInfo not found");
        }

        Orders orders = new Orders();
        orders.setUser(userUtilService.getCurrentUser());
        orders.setOrderStatus(OrderStatus.PENDING);
        orders.setTotalAmount(cart.get().getDiscountedPrice());
        orders.setOrderItems(createListOrderItem(orders));
        orders.setAddress(address.get().getAddress());
        orders.setNumberPhone(profile.get().getPhoneNumber());

        // Shipping fee
        double shippingFee = ghnService.calculateShippingFee(addressInfo.getDistrictId(), addressInfo.getWardCode(), orders.getOrderItems().size() * 348);
        orders.setShippingFee(shippingFee);
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

    public static String extractCode(String description) {
        String prefix = "DC24";
        int index = description.indexOf(prefix);
        if (index != -1 && index + 10 <= description.length()) {
            return description.substring(index, index + 10);
        }
        return null;
    }
}
