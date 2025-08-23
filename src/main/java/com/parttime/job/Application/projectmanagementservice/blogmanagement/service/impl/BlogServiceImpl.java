package com.parttime.job.Application.projectmanagementservice.blogmanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.common.utils.ValidationUtil;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.constant.MessageConstantBlog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.*;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogInteractiveResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Blog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Category;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.mapper.BlogMapper;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.repository.BlogRepository;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.repository.CategoryRepository;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.service.BlogService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    private final BlogMapper blogMapper;
    private static final int PAGE_SIZE_INDEX = 1;

//    private final NotificationService notificationService;

    @Override
    public BlogResponse createBlog(CreateBlogRequest blogRequest) {
        Optional<Category> category = categoryRepository.findById(blogRequest.getCategoryId());
        if (category.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.CATEGORY_NOT_FOUND);
        }
        Optional<User> user = userRepository.findById(blogRequest.getUserId());
        if (user.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.USER_NOT_FOUND);
        }
        Blog blog = blogMapper.toBlog(blogRequest, user.get(), category.get());

        String baseSlug = ValidationUtil.toSlug(blogRequest.getTitle());
        String uniqueSlug = ensureUniqueSlugName(baseSlug);
        blog.setSlugName(uniqueSlug);
        blogRepository.save(blog);
//        NotificationEvent event = NotificationEvent
//                .builder()
//                .param(Map.of("blogName", blogRequest.getTitle(), "inLocation", blog.getId(), "slugName", blog.getSlugName()))
//                .build();
//        notificationService.sendBlogNotification(event);
        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public PagingResponse<BlogResponse> getAllBlog(String searchText, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Blog> blogPage = blogRepository.getAllBlogBySearch(searchText, pageRequest);
        if (blogPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        List<BlogResponse> blogResponse = blogMapper.toListBlogResponse(blogPage.getContent());
        return new PagingResponse<>(blogResponse, pagingRequest, blogPage.getTotalElements());
    }

    @Override
    public PagingResponse<BlogResponse> searchBlog(SearchBlogByField searchBlogByField, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Blog> blogPage = blogRepository.searchBlog(searchBlogByField.getTitle(), searchBlogByField.getContent(), searchBlogByField.getCategory(), pageRequest);
        if (blogPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstant.DATA_NOT_FOUND);
        }
        List<BlogResponse> blogResponse = blogMapper.toListBlogResponse(blogPage.getContent());
        return new PagingResponse<>(blogResponse, pagingRequest, blogPage.getTotalElements());

    }

    @Override
    public BlogResponse findBlogBySlugName(String slugName) {
        Optional<Blog> blog = blogRepository.findBySlugName(slugName);
        if (blog.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        return blogMapper.toBlogResponse(blog.get());
    }

    @Override
    public BlogResponse updateBlog(UpdateRequestBlog updateRequestBlog, String slugName) {
        Optional<Blog> optionalBlog = blogRepository.findBySlugName(slugName);
        if (optionalBlog.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }

        Blog blog = optionalBlog.get();

        if (updateRequestBlog.getTitle() != null && !updateRequestBlog.getTitle().trim().isEmpty()) {
            blog.setTitle(updateRequestBlog.getTitle());
            String baseSlug = ValidationUtil.toSlug(updateRequestBlog.getTitle());
            if (!baseSlug.equals(blog.getSlugName())) {
                String uniqueSlug = ensureUniqueSlugName(baseSlug);
                blog.setSlugName(uniqueSlug);
            }
        }

        if (updateRequestBlog.getContent() != null && !updateRequestBlog.getContent().trim().isEmpty()) {
            blog.setContent(updateRequestBlog.getContent());
        }

        if (updateRequestBlog.getImage() != null && !updateRequestBlog.getImage().trim().isEmpty()) {
            blog.setImage(updateRequestBlog.getImage());
        }

        if (updateRequestBlog.getCategoryId() != null && !updateRequestBlog.getCategoryId().trim().isEmpty()) {
            Optional<Category> categoryOptional = categoryRepository.findById(updateRequestBlog.getCategoryId());
            if (categoryOptional.isPresent()) {
                blog.setCategory(categoryOptional.get());
            } else {
                throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.CATEGORY_NOT_FOUND);
            }
        }

        blogRepository.save(blog);
        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public BlogResponse updateBlogShow(String slugName, BlogShowRequest show) {
        Optional<Blog> blog = blogRepository.findBySlugName(slugName);
        if (blog.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        if (show == null) {
            throw new AppException(MessageCodeConstant.M016_VALIDATION_FAILED, MessageConstantBlog.VALUE_NOT_NULL);
        }
        blog.get().setView(show.getShow());

        blogRepository.save(blog.get());
        return blogMapper.toBlogResponse(blog.get());
    }


    @Override
    public void deleteBlog(String blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        blogRepository.delete(blog.get());
    }

    @Override
    public List<BlogInteractiveResponse> findFiveBlogInteractive(String categoryName) {
        Pageable topFive = PageRequest.of(0, 5);
        List<Blog> blogs = blogRepository.findFiveBlogInteractive(categoryName, topFive);
        return blogMapper.toListBlogInteractiveResponse(blogs);
    }

    @Override
    public void updateEmojiBlog(String blogId, UpdateEmojisRequest emojisRequest) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        if (emojisRequest.getEmojis() < 0) {
            throw new AppException(MessageCodeConstant.M005_INVALID, MessageConstantBlog.UPDATE_EMOJIS);
        }
        blog.get().setEmojis(emojisRequest.getEmojis());
        blogRepository.save(blog.get());

    }

    @Override
    public BlogResponse findBlogByBlogId(String blogId) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        return blogMapper.toBlogResponse(blog.get());
    }

    @Override
    public PagingResponse<BlogResponse> getBlogByCategory(String searchText, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Blog> blogPage = blogRepository.getAllBlogByCategory(searchText, pageRequest);
        if (blogPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }
        List<BlogResponse> blogResponse = blogMapper.toListBlogResponse(blogPage.getContent());
        return new PagingResponse<>(blogResponse, pagingRequest, blogPage.getTotalElements());

    }

    @Override
    public BlogResponse updateBlogById(UpdateRequestBlog updateRequestBlog, String blogId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if (optionalBlog.isEmpty()) {
            throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.BLOG_NOT_FOUND);
        }

        Blog blog = optionalBlog.get();

        if (updateRequestBlog.getTitle() != null && !updateRequestBlog.getTitle().trim().isEmpty()) {
            blog.setTitle(updateRequestBlog.getTitle());
            String baseSlug = ValidationUtil.toSlug(updateRequestBlog.getTitle());
            if (!baseSlug.equals(blog.getSlugName())) {
                String uniqueSlug = ensureUniqueSlugName(baseSlug);
                blog.setSlugName(uniqueSlug);
            }
        }

        if (updateRequestBlog.getContent() != null && !updateRequestBlog.getContent().trim().isEmpty()) {
            blog.setContent(updateRequestBlog.getContent());
        }

        if (updateRequestBlog.getImage() != null && !updateRequestBlog.getImage().trim().isEmpty()) {
            blog.setImage(updateRequestBlog.getImage());
        }

        if (updateRequestBlog.getCategoryId() != null && !updateRequestBlog.getCategoryId().trim().isEmpty()) {
            Optional<Category> categoryOptional = categoryRepository.findById(updateRequestBlog.getCategoryId());
            if (categoryOptional.isPresent()) {
                blog.setCategory(categoryOptional.get());
            } else {
                throw new AppException(MessageConstant.DATA_NOT_FOUND, MessageConstantBlog.CATEGORY_NOT_FOUND);
            }
        }

        blogRepository.save(blog);
        return blogMapper.toBlogResponse(blog);
    }

    private String ensureUniqueSlugName(String baseSlug) {
        String slugToUse = baseSlug;
        
        while (blogRepository.existsBySlugName(slugToUse)) {
            slugToUse = ValidationUtil.appendRandomSuffix(baseSlug);
        }
        
        return slugToUse;
    }
}
