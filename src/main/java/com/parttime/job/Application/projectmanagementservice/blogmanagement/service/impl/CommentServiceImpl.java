package com.parttime.job.Application.projectmanagementservice.blogmanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.constant.MessageConstantBlog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.CommentRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.UpdateCommentRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.UpdateEmojiComment;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.CommentResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Blog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Comment;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.mapper.CommentMapper;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.repository.BlogRepository;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.repository.CommentRepository;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.service.CommentService;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import com.parttime.job.Application.projectmanagementservice.profile.repository.ProfileRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
//    private final NotificationService notificationService;
    private final ProfileRepository profileRepository;

    private static final int PAGE_SIZE_INDEX = 1;

    @Override
    public PagingResponse<CommentResponse> getAllCommentInBlog(String blogId, PagingRequest pagingRequest) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        Sort sort = PagingUtil.createSort(pagingRequest);
        Pageable pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Comment> commentPage = commentRepository.getListCommentByBlogId(blogId, pageRequest);
        List<CommentResponse> commentResponses = commentMapper.toListCommentCommentResponse(commentPage.getContent());
        return new PagingResponse<>(commentResponses, pagingRequest, commentPage.getTotalElements());
    }

    @Override
    public CommentResponse createComment(String blogId, CommentRequest commentRequest) {
        log.warn("Data + +++++++++++++++++++: " +commentRequest);
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        Optional<User> user = userRepository.findById(commentRequest.getUserId());
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.USER_NOT_FOUND);
        }
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setUser(user.get());
        comment.setBlog(blog.get());
        comment.setCreatedBy(commentRequest.getUserId());
        Optional<Comment> commentParent = Optional.empty();
        if (commentRequest.getCommentParentId() != null) {
            commentParent = commentRepository.findById(commentRequest.getCommentParentId());
            if (commentParent.isEmpty()) {
                throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.PARENT_COMMENT_NOT_FOUND);
            }
            if (commentParent.get().getLevel() != 1) {
                commentParent = Optional.ofNullable(commentParent.get().getParentComment());
                if (commentParent.isEmpty()) {
                    throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.INVALID_PARENT_COMMENT);
                }
            }
            comment.setLevel(2);
            comment.setParentComment(commentParent.get());
        } else {
            comment.setLevel(1);
            comment.setParentComment(null);
        }
        comment = commentRepository.save(comment);

//        if (commentParent.isPresent()) {
//            String userCreationName = user.get().getUsername();
//            String userRecuperationId = commentParent.get().getUser().getId();
//            String inLocation = blogId;
//            String slugName = blog.get().getSlugName();
//            String blogName = blog.get().getTitle();
//            String contentComment = comment.getContent();

//            NotificationEvent event = NotificationEvent
//                    .builder()
//                    .param(Map.of(
//                            "userCreationName", userCreationName,
//                            "userRecuperationId", userRecuperationId,
//                            "inLocation", inLocation,
//                            "slugName", slugName,
//                            "blogName", blogName,
//                            "contentComment", contentComment))
//                    .build();
//
//            notificationService.sendCommentNotification(event);
//        }
        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setAvatar(getAvatarFromUser(commentResponse.getUserCreated()));
        return commentResponse;
    }

    @Override
    public CommentResponse updateComment(String blogId, UpdateCommentRequest updateCommentRequest) {
        Optional<Blog> blog = blogRepository.findById(blogId.trim());
        if (blog.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        Optional<Comment> comment = commentRepository.findById(updateCommentRequest.getCommentId().trim());
        if (comment.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.COMMENT_NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(updateCommentRequest.getUserId().trim());
        if (user.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.USER_NOT_FOUND);
        }
        if (comment.get().getUser().equals(user.get())) {


            if (updateCommentRequest.getContent() != null || !updateCommentRequest.getContent().trim().isEmpty()) {
                comment.get().setContent(updateCommentRequest.getContent());
            }
            commentRepository.save(comment.get());
            for (Comment data : blog.get().getComments()) {
                if (data.getId().trim().equals(updateCommentRequest.getCommentId().trim())) {
                    data.setContent(updateCommentRequest.getContent());
                    data.setUpdatedBy(updateCommentRequest.getUserId());
                }
            }
            blogRepository.save(blog.get());
            return commentMapper.toCommentResponse(comment.get());
        }
        throw new AppException(MessageCodeConstant.M006_UNAUTHORIZED, MessageConstant.UNAUTHORIZED);
    }

    @Override
    public void deleteComment(String blogId, String commentId, String userId) {
        Optional<Blog> blog = blogRepository.findById(blogId.trim());
        if (blog.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        Optional<Comment> comment = commentRepository.findById(commentId.trim());
        if (comment.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.COMMENT_NOT_FOUND);
        }
        Optional<User> user = userRepository.findById(userId.trim());
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.USER_NOT_FOUND);
        }
        if (!user.get().equals(comment.get().getUser())) {
            throw new AppException(MessageCodeConstant.M006_UNAUTHORIZED, MessageConstant.UNAUTHORIZED);
        }

        Comment commentToDelete = comment.get();
        commentToDelete.setBlog(null);
        commentRepository.delete(commentToDelete);
    }

    @Override
    public void updateEmojisComment(String blogId, String commentId, String userId, UpdateEmojiComment updateEmojiComment) {
        Optional<Blog> blog = blogRepository.findById(blogId.trim());
        if (blog.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        Optional<Comment> comment = commentRepository.findById(commentId.trim());
        if (comment.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.COMMENT_NOT_FOUND);
        }
        Optional<User> user = userRepository.findById(userId.trim());
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.USER_NOT_FOUND);
        }
        comment.get().setEmojis(updateEmojiComment.getEmojis());
        commentRepository.save(comment.get());
        for (Comment data : blog.get().getComments()) {
            if (data.getId().trim().equals(commentId.trim())) {
                data.setEmojis(updateEmojiComment.getEmojis());
            }
        }
        blogRepository.save(blog.get());
    }

    private String getAvatarFromUser(String username){
         Profile profile = profileRepository.getProfileByUserName(username);
        return profile.getAvatar();
    }
}
