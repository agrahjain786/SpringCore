package com.techlabs.app.service;

import com.techlabs.app.dto.BlogDTO;
import com.techlabs.app.dto.CommentDTO;
import com.techlabs.app.util.PagedResponse;

import jakarta.validation.Valid;


public interface BlogService {

	PagedResponse<BlogDTO> getAllBlogs(int page, int size, String sortBy, String direction);

	BlogDTO getBlogById(int blogId);

	BlogDTO addBlog(BlogDTO blogDTO);
	
	BlogDTO updateBlog(BlogDTO blogDTO);

	void deleteBlog(int blogId);

	PagedResponse<BlogDTO> getAllPublishedBlogs(int page, int size, String sortBy, String direction);

	PagedResponse<BlogDTO> getAllUnpublishedBlogs(int page, int size, String sortBy, String direction);

	BlogDTO makeBlogPublished(int blogId);

	BlogDTO addCommentToPost(int blogId, CommentDTO commentDTO);

	BlogDTO deleteCommentToPost(int blogId, int commentId);

	BlogDTO updateCommentToPost(int blogId, @Valid CommentDTO commentDTO);

	

	

}
