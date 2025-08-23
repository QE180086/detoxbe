package com.parttime.job.Application.projectmanagementservice.blogmanagement.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog extends BaseEntity {
    @Column(nullable = false)
    String title;
    @Column(columnDefinition = "TEXT")
    String content;
    @Column
    String image;
    @Column
    int emojis;
    @Column
    boolean view;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "blog")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "slug_name", unique = true)
    String slugName;
}
