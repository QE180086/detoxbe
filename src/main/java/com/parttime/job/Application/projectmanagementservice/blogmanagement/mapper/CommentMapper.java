package com.parttime.job.Application.projectmanagementservice.blogmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.CommentResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Comment;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import com.parttime.job.Application.projectmanagementservice.profile.repository.ProfileRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Autowired
    protected ProfileRepository profileRepository;

    @Mapping(target = "userCreated", source = "user.username")
    @Mapping(target = "avatar", expression = "java(getAvatarFromUser(comment.getUser()))")
    public abstract CommentResponse toCommentResponse(Comment comment);

    public abstract List<CommentResponse> toListCommentCommentResponse(List<Comment> comments);

    protected String getAvatarFromUser(User user) {
        if (user == null) {
            return null;
        }
        Optional<Profile> profile = findProfileByUser(user);
        return profile != null ? profile.get().getAvatar() : null;
    }

    protected Optional<Profile> findProfileByUser(User user) {
        return user.getId() != null ? profileRepository.findByUserId(user.getId()) : null;
    }
}
