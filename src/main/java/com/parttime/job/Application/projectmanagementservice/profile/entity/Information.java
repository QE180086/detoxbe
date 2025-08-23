package com.parttime.job.Application.projectmanagementservice.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parttime.job.Application.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Information extends BaseEntity {
    private String facebook;
    private String instagram;
    private String tiktok;
    private String zalo;
    private String twitter;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @JsonIgnore
    private Profile profile;
}
