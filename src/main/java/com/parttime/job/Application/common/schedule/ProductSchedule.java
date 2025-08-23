package com.parttime.job.Application.common.schedule;

import com.parttime.job.Application.projectmanagementservice.aisupport.service.impl.AISupportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProductSchedule {
    private final AISupportServiceImpl aiSupport;

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void scheduledDetoxCacheRefresh() {
        aiSupport.refreshDetoxCache();
        System.out.println("Detox cache refreshed at " + LocalDateTime.now());
    }
}
