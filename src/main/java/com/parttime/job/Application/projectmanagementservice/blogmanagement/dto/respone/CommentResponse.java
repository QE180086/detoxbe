package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Blog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Comment;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    Date createdDate;
    int emojis;
    String content;
    int level;
    @JsonIgnore
    Blog blog;
    String userCreated;
    @JsonIgnore
    Comment parentComment;
    List<CommentResponse> commentChild = new ArrayList<>();
    String avatar;
}
