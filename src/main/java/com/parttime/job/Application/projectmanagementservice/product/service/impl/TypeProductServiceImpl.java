package com.parttime.job.Application.projectmanagementservice.product.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.product.constant.ProductConstant;
import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.product.entity.TypeProduct;
import com.parttime.job.Application.projectmanagementservice.product.mapper.TypeProductMapper;
import com.parttime.job.Application.projectmanagementservice.product.repository.TypeProductRepository;
import com.parttime.job.Application.projectmanagementservice.product.request.TypeProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.request.UpdateTypeProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductResponse;
import com.parttime.job.Application.projectmanagementservice.product.response.TypeProductResponse;
import com.parttime.job.Application.projectmanagementservice.product.service.TypeProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypeProductServiceImpl implements TypeProductService {
    private final TypeProductRepository typeProductRepository;
    private final TypeProductMapper typeProductMapper;

    @Override
    public TypeProductResponse create(TypeProductRequest request) {
        TypeProduct typeProduct = new TypeProduct();
        typeProduct.setDeleted(false);
        typeProduct.setName(request.getName());
        typeProduct.setDescription(request.getDescription());
        typeProduct.setImage(request.getImage());
        typeProductRepository.save(typeProduct);
        return typeProductMapper.toDTO(typeProduct);
    }

    @Override
    public TypeProductResponse update(UpdateTypeProductRequest request) {
        TypeProduct typeProduct = typeProductRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại sản phẩm với ID: " + request.getId()));

        boolean updated = false;

        if (request.getName() != null && !request.getName().equals(typeProduct.getName())) {
            typeProduct.setName(request.getName());
            updated = true;
        }

        if (request.getDescription() != null && !request.getDescription().equals(typeProduct.getDescription())) {
            typeProduct.setDescription(request.getDescription());
            updated = true;
        }

        if (request.getImage() != null && !request.getImage().equals(typeProduct.getImage())) {
            typeProduct.setImage(request.getImage());
            updated = true;
        }

        if (updated) {
            typeProductRepository.save(typeProduct);
            log.info("TypeProduct với ID {} đã được cập nhật", request.getId());
        } else {
            log.info("Không có thay đổi nào với TypeProduct ID {}", request.getId());
        }

        return typeProductMapper.toDTO(typeProduct);
    }

    @Override
    public PagingResponse<TypeProductResponse> getAllTypeProducts(String searchText, PagingRequest request) {
            Sort sort = PagingUtil.createSort(request);
            PageRequest pageRequest = PageRequest.of(
                    request.getPage() - PAGE_SIZE_INDEX,
                    request.getSize(),
                    sort
            );
            Page<TypeProduct> typeProductPage = typeProductRepository.searchTypeProductsByName(searchText, pageRequest);
            if (typeProductPage == null) {
                throw new AppException(MessageCodeConstant.M003_NOT_FOUND, ProductConstant.PRODUCT_NOT_FOUND);
            }
            List<TypeProductResponse> typeProductResponse = typeProductMapper.toDTOs(typeProductPage.getContent());
            return new PagingResponse<>(typeProductResponse, request, typeProductPage.getTotalElements());
    }
}
