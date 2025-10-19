package com.parttime.job.Application.projectmanagementservice.configuration.applicationlistener;

import com.parttime.job.Application.projectmanagementservice.shippingmanagement.service.GhnMasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitApplicationReadyEvent {
    private final GhnMasterDataService ghnMasterDataService;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        ghnMasterDataService.loadAllData();
    }
}
