package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Comment;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogResponse {
    String id;
    Date createdDate;
    String title;
    String content;
    String image;
    int emojis;
    boolean view;
    String userName;
    String fullname;
    String categoryName;
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();
    String slugName;
}
