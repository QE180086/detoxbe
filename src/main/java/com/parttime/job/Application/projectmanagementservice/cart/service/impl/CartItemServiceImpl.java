package com.parttime.job.Application.projectmanagementservice.cart.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import com.parttime.job.Application.projectmanagementservice.cart.entity.CartItem;
import com.parttime.job.Application.projectmanagementservice.cart.mapper.CartItemMapper;
import com.parttime.job.Application.projectmanagementservice.cart.mapper.CartMapper;
import com.parttime.job.Application.projectmanagementservice.cart.repository.CartItemRepository;
import com.parttime.job.Application.projectmanagementservice.cart.repository.CartRepository;
import com.parttime.job.Application.projectmanagementservice.cart.request.CartItemRequest;
import com.parttime.job.Application.projectmanagementservice.cart.request.UpdateCartItemRequest;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartItemResponse;
import com.parttime.job.Application.projectmanagementservice.cart.service.CartItemService;
import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.product.repository.ProductRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;
    private final CartMapper cartMapper;
    private final UserUtilService userUtilService;

    @Transactional
    @Override
    public CartItemResponse addCartItem(CartItemRequest request) {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "User not authenticated");
        }
        Optional<Cart> cart = cartRepository.findByUserIdAndIsActiveTrue(user.getId());
        if (cart.isEmpty()) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setActive(true);
            cartRepository.save(newCart);
            cart = Optional.of(newCart);
        }
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Product not found"));

        Optional<CartItem> cartItem = cartItemRepository.findByCartIdAndProductIdAndCartIsActiveTrue(cart.get().getId(), product.getId());
        if (cartItem.isEmpty()) {
            CartItem newCartItem = new CartItem();
            newCartItem.setQuantity(1);
            newCartItem.setProduct(product);
            newCartItem.setCart(cart.get());
            if (product.getSalePrice() == 0) {
                newCartItem.setUnitPrice(product.getPrice());
            } else {
                newCartItem.setUnitPrice(product.getSalePrice());
            }
            cartItemRepository.save(newCartItem);
            return cartItemMapper.toDTO(newCartItem);
        }
        cartItem.get().setQuantity(cartItem.get().getQuantity() + 1);
        if (product.getSalePrice() == 0) {
            cartItem.get().setUnitPrice(product.getPrice());
        } else {
            cartItem.get().setUnitPrice(product.getSalePrice());
        }
        cartItemRepository.saveAndFlush(cartItem.get());
        return cartItemMapper.toDTO(cartItem.get());
    }

    @Override
    public PagingResponse<CartItemResponse> getCartItemsByCartId(PagingRequest pagingRequest) {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "User not authenticated");
        }
        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart not found for user"));

        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<CartItem> cartItemPage = cartItemRepository.getAllCartItem(cart.getId(), pageRequest);
        if (cartItemPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart Item is not found");
        }
        List<CartItemResponse> cartItemResponse = cartItemMapper.toListDTO(cartItemPage.getContent());
        return new PagingResponse<>(cartItemResponse, pagingRequest, cartItemPage.getTotalElements());
    }

    @Override
    public CartItemResponse getCartItemById(String cartItemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart item not found");
        }
        return cartItemMapper.toDTO(cartItem.get());

    }

    @Override
    public CartItemResponse updateCartItem(UpdateCartItemRequest request) {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "User not authenticated");
        }
        Optional<CartItem> cartItem = cartItemRepository.findById(request.getCartItemId());
        cartItem.get().setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem.get());
        return cartItemMapper.toDTO(cartItem.get());
    }

    @Override
    public void removeCartItem(String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void removeAllItemsFromCart() {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "User not authenticated");
        }
        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart not found for user"));

        cartItemRepository.deleteAllByCart_Id(cart.getId());
    }
}
