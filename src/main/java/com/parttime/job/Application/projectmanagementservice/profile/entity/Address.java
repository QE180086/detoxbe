package com.parttime.job.Application.projectmanagementservice.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parttime.job.Application.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address extends BaseEntity {
    private String street;
    private String city;
    private String state;
    private String country;
    private boolean isDefault;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @JsonIgnore
    private Profile profile;
    private String other;
}
