package com.parttime.job.Application.projectmanagementservice.shippingmanagement.service;

import com.parttime.job.Application.projectmanagementservice.shippingmanagement.cache.GhnMasterDataCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GhnMasterDataService {

    private final RestTemplate restTemplate;
    private final GhnMasterDataCache cache;

    @Value("${ghn.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data";

    public void loadAllData() {
        log.info(" Loading GHN master data into cache...");
        cache.clearAll();
        loadProvinces();
        loadDistricts();
        loadWards();
        log.info(" GHN master data cache ready (provinces={}, districts={}, wards={})",
                cache.getProvinces().size(), cache.getDistricts().size(), cache.getWards().size());
    }

    private void loadProvinces() {
        String url = BASE_URL + "/province";
        HttpEntity<Void> entity = new HttpEntity<>(headers());
        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONArray arr = new JSONObject(res.getBody()).getJSONArray("data");

        arr.forEach(obj -> {
            JSONObject p = (JSONObject) obj;
            cache.getProvinces().put(p.getInt("ProvinceID"), p.getString("ProvinceName"));
        });
    }

    private void loadDistricts() {
        String url = BASE_URL + "/district";

        for (Integer provinceId : cache.getProvinces().keySet()) {
            Map<String, Object> body = new HashMap<>();
            body.put("province_id", provinceId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers());

            try {
                ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                JSONObject json = new JSONObject(res.getBody());

                if (!json.isNull("data")) {
                    JSONArray arr = json.getJSONArray("data");

                    arr.forEach(obj -> {
                        JSONObject d = (JSONObject) obj;

                        List<String> nameExtensions = new ArrayList<>();
                        if (d.has("NameExtension") && !d.isNull("NameExtension")) {
                            JSONArray extArr = d.getJSONArray("NameExtension");
                            for (int i = 0; i < extArr.length(); i++) {
                                nameExtensions.add(extArr.getString(i));
                            }
                        }

                        cache.getDistricts().put(
                                d.getInt("DistrictID"),
                                new GhnMasterDataCache.District(
                                        d.getInt("DistrictID"),
                                        d.getString("DistrictName"),
                                        d.getInt("ProvinceID"),
                                        nameExtensions
                                )
                        );
                    });
                }

                Thread.sleep(100); // tránh rate limit GHN
            } catch (Exception e) {
                log.warn("Not loaded district for province {}: {}", provinceId, e.getMessage());
            }
        }
    }


    private void loadWards() {
        for (Integer districtId : cache.getDistricts().keySet()) {
            String url = BASE_URL + "/ward?district_id=" + districtId;
            HttpEntity<Void> entity = new HttpEntity<>(headers());
            try {
                ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                JSONObject json = new JSONObject(res.getBody());

                if (!json.isNull("data")) {
                    JSONArray arr = json.getJSONArray("data");

                    arr.forEach(obj -> {
                        JSONObject w = (JSONObject) obj;

                        List<String> nameExtensions = new ArrayList<>();
                        if (w.has("NameExtension") && !w.isNull("NameExtension")) {
                            JSONArray extArr = w.getJSONArray("NameExtension");
                            for (int i = 0; i < extArr.length(); i++) {
                                nameExtensions.add(extArr.getString(i));
                            }
                        }
                        cache.getWards().put(
                                w.getString("WardCode"),
                                new GhnMasterDataCache.Ward(
                                        w.getString("WardCode"),
                                        w.getString("WardName"),
                                        districtId,
                                        nameExtensions
                                )
                        );
                    });
                }

                Thread.sleep(100);
            } catch (Exception e) {
                log.warn("Not loaded ward cho district {}: {}", districtId, e.getMessage());
            }
        }
    }


    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}