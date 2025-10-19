package com.parttime.job.Application.projectmanagementservice.shippingmanagement.cache;


import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Getter
public class GhnMasterDataCache {

    private final Map<Integer, String> provinces = new ConcurrentHashMap<>();
    private final Map<Integer, District> districts = new ConcurrentHashMap<>();
    private final Map<String, Ward> wards = new ConcurrentHashMap<>();

    @Getter
    public static class District {
        private final int id;
        private final String name;
        private final int provinceId;
        private final List<String> nameExtensions;

        public District(int id, String name, int provinceId, List<String> nameExtensions) {
            this.id = id;
            this.name = name;
            this.provinceId = provinceId;
            this.nameExtensions = nameExtensions;
        }
    }

    @Getter
    @ToString
    public static class Ward {
        private final String code;
        private final String name;
        private final int districtId;
        private final List<String> nameExtensions;

        public Ward(String code, String name, int districtId, List<String> nameExtensions) {
            this.code = code;
            this.name = name;
            this.districtId = districtId;
            this.nameExtensions = nameExtensions;
        }
    }

    public void clearAll() {
        provinces.clear();
        districts.clear();
        wards.clear();
        log.info(" Cleared GHN master data cache");
    }
}