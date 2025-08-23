package com.parttime.job.Application.projectmanagementservice.blogmanagement.service;


import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.*;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogInteractiveResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogResponse;

import java.util.List;

public interface BlogService {

    /**
     * Creates a new blog.
     *
     * @param blogRequest The data of the blog to be created.
     * @return The information of the newly created blog.
     */
    BlogResponse createBlog(CreateBlogRequest blogRequest);

    /**
     * Retrieves all blogs with pagination support.
     *
     * @param pagingRequest The pagination request parameters.
     * @return A paginated list of blog responses.
     */
    PagingResponse<BlogResponse> getAllBlog(String searchText, PagingRequest pagingRequest);

    /**
     * Searches blogs based on specified fields with pagination.
     *
     * @param searchBlogByField The search criteria.
     * @param pagingRequest     The pagination request parameters.
     * @return A paginated list of blog responses matching the search criteria.
     */
    PagingResponse<BlogResponse> searchBlog(SearchBlogByField searchBlogByField, PagingRequest pagingRequest);

    /**
     * Finds a blog by its unique identifier.
     *
     * @param slugName The unique slugName of the blog.
     * @return The blog details if found.
     */
    BlogResponse findBlogBySlugName(String slugName);

    /**
     * Updates an existing blog.
     *
     * @param updateRequestBlog The updated blog details.
     * @param slugName          The unique slugName of the blog to be updated.
     * @return The updated blog information.
     */
    BlogResponse updateBlog(UpdateRequestBlog updateRequestBlog, String slugName);

    /**
     * Updates the visibility status of a blog.
     *
     * @param slugName The unique slugName of the blog.
     * @param show   The request containing visibility details.
     * @return The updated blog with the new visibility status.
     */
    BlogResponse updateBlogShow(String slugName, BlogShowRequest show);

    /**
     * Deletes a blog by its unique identifier.
     *
     * @param blogId The unique ID of the blog to be deleted.
     *               A confirmation message or status.
     */
    void deleteBlog(String blogId);

    /**
     * Find five blog interactive blog.
     *
     * @param categoryName categoryName.
     * @return List<BlogInteractiveResponse>
     */
    List<BlogInteractiveResponse> findFiveBlogInteractive(String categoryName);

    /**
     * Update Emoji Blog
     *
     * @param blogId        The unique ID of the blog to be find Blog.
     * @param emojisRequest number to update emojis request.
     */
    void updateEmojiBlog(String blogId, UpdateEmojisRequest emojisRequest);

    /**
     * Finds a blog by id.
     *
     * @param blogId The unique blogId of the blog.
     * @return The blog details if found.
     */
    BlogResponse findBlogByBlogId(String blogId);


    /**
     * Retrieves all blogs by Category with pagination support.
     *
     * @param pagingRequest The pagination request parameters.
     * @param searchText searchText
     *
     * @return A paginated list of blog responses.
     */
    PagingResponse<BlogResponse> getBlogByCategory(String searchText, PagingRequest pagingRequest);

    /**
     * Updates an existing blog.
     *
     * @param updateRequestBlog The updated blog details.
     * @param blogId          The unique blogId of the blog to be updated.
     * @return The updated blog information.
     */
    BlogResponse updateBlogById(UpdateRequestBlog updateRequestBlog, String blogId);
}
