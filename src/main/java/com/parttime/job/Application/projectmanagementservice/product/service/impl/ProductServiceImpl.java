package com.parttime.job.Application.projectmanagementservice.product.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.product.constant.ProductConstant;
import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.product.entity.TypeProduct;
import com.parttime.job.Application.projectmanagementservice.product.mapper.ProductMapper;
import com.parttime.job.Application.projectmanagementservice.product.repository.ProductRepository;
import com.parttime.job.Application.projectmanagementservice.product.repository.TypeProductRepository;
import com.parttime.job.Application.projectmanagementservice.product.request.ProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductResponse;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductStatsResponse;
import com.parttime.job.Application.projectmanagementservice.product.service.ProductService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final UserUtilService userUtilService;

    private final TypeProductRepository typeProductRepository;

    @Override
    public PagingResponse<ProductResponse> getListProduct(String searchText, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Product> productPage = productRepository.searchProductsByName(searchText, pageRequest);
        if (productPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, ProductConstant.PRODUCT_NOT_FOUND);
        }
        List<ProductResponse> productResponse = productMapper.toListDTO(productPage.getContent());
        return new PagingResponse<>(productResponse, pagingRequest, productPage.getTotalElements());
    }

    @Override
    public ProductResponse getDetailProduct(String productId) {
        Optional<Product> comboBooking = productRepository.findById(productId);
        if (comboBooking.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, ProductConstant.PRODUCT_NOT_FOUND);
        }
        return productMapper.toDTO(comboBooking.get());
    }

    @Override
    public ProductResponse create(ProductRequest productRequest) {

        if(!typeProductRepository.existsByIdAndIsDeletedFalse(productRequest.getTypeProductId())){
            throw new AppException(MessageCodeConstant.M026_FAIL, ProductConstant.TYPE_PRODUCT_NOT_FOUND);
        }
       Optional<TypeProduct> typeProduct = typeProductRepository.findById(productRequest.getTypeProductId());
        Product product = new Product();

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImage(productRequest.getImage());
        product.setTypeProduct(typeProduct.get());
        product.setPrice(productRequest.getPrice());
        product.setActive(productRequest.isActive());
        product.setCreatedBy(userUtilService.getIdCurrentUser());
        product.setSalePrice(productRequest.getSalePrice());
        productRepository.save(product);

        return productMapper.toDTO(product);
    }

    @Override
    public ProductResponse update(String productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, ProductConstant.PRODUCT_NOT_FOUND));

        TypeProduct typeProduct = typeProductRepository.findById(productRequest.getTypeProductId())
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "TypeProduct not found"));

        if (StringUtils.hasText(productRequest.getName()) && !product.getName().equals(productRequest.getName())) {
            product.setName(productRequest.getName());
        }

        if (StringUtils.hasText(productRequest.getDescription()) && !product.getDescription().equals(productRequest.getDescription())) {
            product.setDescription(productRequest.getDescription());
        }

        if (StringUtils.hasText(productRequest.getImage()) && !product.getImage().equals(productRequest.getImage())) {
            product.setImage(productRequest.getImage());
        }

        if (product.getPrice() != productRequest.getPrice()) {
            product.setPrice(productRequest.getPrice());
        }

        if (product.getSalePrice() != productRequest.getSalePrice()) {
            product.setSalePrice(productRequest.getSalePrice());
        }

        if (product.isActive() != productRequest.isActive()) {
            product.setActive(productRequest.isActive());
        }

        if (!product.getTypeProduct().getId().equals(productRequest.getTypeProductId())) {
            product.setTypeProduct(typeProduct);
        }

        productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public void delete(String productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, ProductConstant.PRODUCT_NOT_FOUND);
        }
        productRepository.delete(product.get());
    }

    @Override
    public ProductStatsResponse getProductStats() {
        Long totalProducts = productRepository.countTotalProducts();
        Long todayProducts = productRepository.countTodayProducts();

        double todayPercent = 0.0;
        if (totalProducts != null && totalProducts > 0) {
            todayPercent = (todayProducts.doubleValue() / totalProducts.doubleValue()) * 100.0;
        }

        return new ProductStatsResponse(totalProducts, todayPercent);    }

//    @Override
//    public Integer countAllProduct(TypeTarget typeTarget) {
//        if (typeTarget == null) {
//            return Math.toIntExact(productRepository.count());
//        }
//        return switch (typeTarget) {
//            case PERSON -> productRepository.countByTypeTarget(TypeTarget.PERSON);
//            case SHOP -> productRepository.countByTypeTarget(TypeTarget.SHOP);
//        };
//    }
//
//    private void checkExistNameInComboBooking(String name, String comboBookingId){
//        if (productRepository.existsByNameAndIdNot(name, comboBookingId)) {
//            throw new AppException(MessageCodeConstant.M004_DUPLICATE, ProductConstant.COMBOBOOKING_IS_EXISTED);
//        }
//    }
}
