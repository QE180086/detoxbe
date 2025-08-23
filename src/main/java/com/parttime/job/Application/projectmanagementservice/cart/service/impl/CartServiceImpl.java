package com.parttime.job.Application.projectmanagementservice.cart.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import com.parttime.job.Application.projectmanagementservice.cart.mapper.CartMapper;
import com.parttime.job.Application.projectmanagementservice.cart.repository.CartRepository;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartResponse;
import com.parttime.job.Application.projectmanagementservice.cart.service.CartService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import com.parttime.job.Application.projectmanagementservice.voucher.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private UserUtilService userUtilService;
    private VoucherRepository voucherRepository;
    private CartMapper cartMapper;

    @Override
    public CartResponse getOrCreateCartByUserId() {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M006_UNAUTHORIZED, "User not authenticated");
        }
        if (!cartRepository.existsByUserIdAndIsActiveTrue(user.getId())) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setActive(true);
            cartRepository.save(newCart);
            return cartMapper.toDTO(newCart);
        }
        return cartMapper.toDTO(cartRepository.findByUserIdAndIsActiveTrue(user.getId()).orElseThrow(() ->
                new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart not found for user")));
    }

    @Override
    public PagingResponse<CartResponse> getListCart(String searchText, PagingRequest request) {
        Sort sort = PagingUtil.createSort(request);
        PageRequest pageRequest = PageRequest.of(
                request.getPage() - PAGE_SIZE_INDEX,
                request.getSize(),
                sort
        );
        Page<Cart> cartPage = cartRepository.getAllCartBySearch(searchText, pageRequest);
        if (cartPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart is not found");
        }
        List<CartResponse> cartResponse = cartMapper.toListDTO(cartPage.getContent());
        return new PagingResponse<>(cartResponse, request, cartPage.getTotalElements());
    }

    @Override
    public CartResponse getCartByCartId(String cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (cart.get() == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart not found with ID: " + cartId);
        }
        return cartMapper.toDTO(cart.get());
    }

    @Override
    public CartResponse applyVoucherToCart(String voucherCode) {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M006_UNAUTHORIZED, "User not authenticated");
        }
        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart not found for user"));

        Voucher voucher = voucherRepository.findByCode(voucherCode);
        if (voucher == null) {
            throw new AppException(MessageCodeConstant.M026_FAIL, "Invalid voucher code");
        }
        if (!voucher.isActive()) {
            throw new AppException(MessageCodeConstant.M026_FAIL, "Voucher is not active");
        }
        if (voucher.getMinOrderValue() <= cart.getTotalPrice()) {
            if (voucher.isPercentage()) {
                cart.setDiscountedPrice(cart.getTotalPrice() * (1 - voucher.getDiscountValue() / 100));
            } else {
                double discountValue = cart.getTotalPrice() - voucher.getDiscountValue();
                if (discountValue < 0) {
                    discountValue = 0.0;
                }
                cart.setDiscountedPrice(discountValue);
            }
            cart.setVoucher(voucher);
            cartRepository.save(cart);
        }
        return cartMapper.toDTO(cart);
    }

    private Map<Boolean, Voucher> validateApplyVoucherToCart(String voucherCode) {
        Voucher voucher = voucherRepository.findByCode(voucherCode);
        if (voucher == null) {
            throw new AppException(MessageCodeConstant.M026_FAIL, "Invalid voucher code");
        }
        if (!voucher.isActive()) {
            throw new AppException(MessageCodeConstant.M026_FAIL, "Voucher is not active");
        }
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M006_UNAUTHORIZED, "User not authenticated");
        }
        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart not found for user"));
        if (voucher.getMinOrderValue() <= cart.getTotalPrice()) {
            if (voucher.isPercentage()) {
                cart.setDiscountedPrice(cart.getTotalPrice() * (1 - voucher.getDiscountValue() / 100));
            } else {
                double discountValue = cart.getTotalPrice() - voucher.getDiscountValue();
                if (discountValue < 0) {
                    discountValue = 0.0;
                }
                cart.setDiscountedPrice(discountValue);
            }
            cartRepository.save(cart);
        }
        return Map.of(true, voucher);
    }
}
