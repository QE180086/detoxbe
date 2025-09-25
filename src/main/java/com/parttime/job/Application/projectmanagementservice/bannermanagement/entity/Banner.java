package com.parttime.job.Application.projectmanagementservice.bannermanagement.entity;
import com.parttime.job.Application.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Banner extends BaseEntity {
    private String title;
    private String content;
    private String image;
    private String url;
    private String createdUser;
}
