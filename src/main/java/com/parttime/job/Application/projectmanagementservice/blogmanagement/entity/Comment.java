package com.parttime.job.Application.projectmanagementservice.blogmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
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

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {
    @Column
    int emojis;

    @Column(columnDefinition = "TEXT")
    String content;

    int level;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonBackReference
    Blog blog;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference("parent-child")
    Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("parent-child")
    private List<Comment> commentChild = new ArrayList<>();

}
