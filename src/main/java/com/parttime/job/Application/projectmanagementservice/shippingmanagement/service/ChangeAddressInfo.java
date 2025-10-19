package com.parttime.job.Application.projectmanagementservice.shippingmanagement.service;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.utils.TextUtil;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.cache.GhnMasterDataCache;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.response.AddressInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangeAddressInfo {
    private final GhnMasterDataCache cache;

    public AddressInfo resolveAddress(String fullAddress) {
        String normalized = TextUtil.normalize(fullAddress);

        Integer provinceId = cache.getProvinces().entrySet().stream()
                .filter(e -> normalized.contains(TextUtil.normalize(e.getValue())))
                .map(e -> e.getKey())
                .findFirst()
                .orElse(null);

        if (provinceId == null)
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Not found address in list province.");
log.info("provinceId: "+ provinceId);
        GhnMasterDataCache.District district = cache.getDistricts().values().stream()
                .filter(d -> d.getProvinceId() == provinceId &&
                        (
                                TextUtil.normalize(d.getName()).contains(normalized) ||
                                        normalized.contains(TextUtil.normalize(d.getName())) ||
                                        d.getNameExtensions().stream()
                                                .map(TextUtil::normalize)
                                                .anyMatch(ext -> normalized.contains(ext) || ext.contains(normalized))
                        )
                )
                .findFirst()
                .orElse(null);
        if (district == null)
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Not found address in list district.");
log.info("district: "+ district.getId());

        GhnMasterDataCache.Ward ward = cache.getWards().values().stream()
                .filter(w -> w.getDistrictId() == district.getId() && (
                        normalized.contains(TextUtil.normalize(w.getName())) ||
                                TextUtil.normalize(w.getName()).contains(normalized) ||
                                w.getNameExtensions().stream()
                                        .map(TextUtil::normalize)
                                        .anyMatch(ext -> normalized.contains(ext) || ext.contains(normalized))
                ))
                .findFirst()
                .orElse(null);
        log.info("ward: "+ ward);

        if (ward == null)
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Not found address in list ward.");

        return new AddressInfo(
                provinceId,
                district.getId(),
                ward.getCode(),
                ward.getName(),
                district.getName(),
                cache.getProvinces().get(provinceId)
        );
    }
}
