package com.parttime.job.Application.common.schedule;

import com.parttime.job.Application.projectmanagementservice.shippingmanagement.service.GhnMasterDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class GhnCacheScheduler {

    private final GhnMasterDataService ghnMasterDataService;

    @Scheduled(cron = "0 0 3 * * MON")
    public void refreshWeekly() {
        log.info(" Refreshing GHN master data cache...");
        ghnMasterDataService.loadAllData();
    }
}