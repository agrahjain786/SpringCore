package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.BlogDTO;
import com.techlabs.app.dto.CommentDTO;
import com.techlabs.app.entity.Blog;
import com.techlabs.app.entity.Comment;
import com.techlabs.app.exception.BlogException;
import com.techlabs.app.repository.BlogRepository;
import com.techlabs.app.repository.CommentRepository;
import com.techlabs.app.util.PagedResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;



@Service
public class BlogServiceImpl implements BlogService{
	
	
	private BlogRepository blogRepository;
	
	private CommentRepository commentRepository;

	public BlogServiceImpl(BlogRepository blogRepository,CommentRepository commentRepository) {
		super();
		this.blogRepository = blogRepository;
		this.commentRepository = commentRepository;
	}


	@Override
	public PagedResponse<BlogDTO> getAllBlogs(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Blog> pages = blogRepository.findAll(pageable);
		List<Blog> allBlogs = pages.getContent();
		List<BlogDTO> allBlogsDTO = convertListToDTO(allBlogs);
		
		return new PagedResponse<BlogDTO>(allBlogsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}

	
	@Override
	public BlogDTO getBlogById(int blogId) {
		
		Blog blog = blogRepository.findById(blogId).orElse(null);
		
		if(blog == null) {
			throw new BlogException("No Blog found with this id "+ blogId);
		}
		
		return convertToBlogDTO(blog);
		
	}
	
	
	@Override
	public BlogDTO addBlog(BlogDTO blogDTO) {
		blogDTO.setId(0);
		if(blogDTO.isPublished() == true) blogDTO.setPublishedDate(LocalDateTime.now());
		else blogDTO.setPublishedDate(null);
		Blog blog = convertToBlogEntity(blogDTO);
		Blog savedBlog = blogRepository.save(blog);
		return convertToBlogDTO(savedBlog);
	}
	
	

	@Override
	public BlogDTO updateBlog(BlogDTO blogDTO) {
		Blog existingBlog = blogRepository.findById(blogDTO.getId()).orElseThrow(() -> new BlogException("Blog not found"));

	    if(blogDTO.isPublished() && !existingBlog.isPublished()) {
	        existingBlog.setPublishedDate(LocalDateTime.now());
	    }

	    existingBlog.setTitle(blogDTO.getTitle());
	    existingBlog.setCategory(blogDTO.getCategory());
	    existingBlog.setData(blogDTO.getData());
	    existingBlog.setPublished(blogDTO.isPublished());
		Blog savedBlog = blogRepository.save(existingBlog);
		return convertToBlogDTO(savedBlog);
	}

	
	
	@Override
	public void deleteBlog(int blogId) {
		Blog existingBlog = blogRepository.findById(blogId).orElseThrow(() -> new BlogException("Blog not found"));
		blogRepository.delete(existingBlog);
	}


	@Override
	public PagedResponse<BlogDTO> getAllPublishedBlogs(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Blog> pages = blogRepository.findByPublishedTrue(pageable);
		List<Blog> allBlogs = pages.getContent();
		List<BlogDTO> allBlogsDTO = convertListToDTO(allBlogs);
		
		return new PagedResponse<BlogDTO>(allBlogsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}


	

	@Override
	public PagedResponse<BlogDTO> getAllUnpublishedBlogs(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
		
		Page<Blog> pages = blogRepository.findByPublishedFalse(pageable);
		List<Blog> allBlogs = pages.getContent();
		List<BlogDTO> allBlogsDTO = convertListToDTO(allBlogs);
		
		return new PagedResponse<BlogDTO>(allBlogsDTO, pages.getNumber(), pages.getSize(), pages.getTotalElements(), pages.getTotalPages(), pages.isLast());
	}
	
	

	@Override
	public BlogDTO makeBlogPublished(int blogId) {
		Blog existingBlog = blogRepository.findById(blogId).orElseThrow(() -> new BlogException("Blog not found"));
		
		if(existingBlog.isPublished() == true) {
			throw new BlogException("Blog with ID "+ blogId + " is already published");
		}
		
		existingBlog.setPublished(true);
		existingBlog.setPublishedDate(LocalDateTime.now());
		
		blogRepository.save(existingBlog);
		return convertToBlogDTO(existingBlog);
	}



	@Override
	public BlogDTO addCommentToPost(int id, CommentDTO commentDTO) {
		Blog existingBlog = blogRepository.findById(id).orElseThrow(() -> new BlogException("Blog not found"));
		
		if(!existingBlog.isPublished()) {
			throw new BlogException("No such Blog Published");
		}
		
		Comment comment = convertToCommentEntity(commentDTO, existingBlog);
		comment.setId(0);
		existingBlog.addComment(comment);
		Blog savedBlog = blogRepository.save(existingBlog);
	    return convertToBlogDTO(savedBlog);
	}


	@Override
	public BlogDTO deleteCommentToPost(int blogId, int commentId) {
		Blog blog = blogRepository.findById(blogId).orElse(null);
		
		if(blog == null || !blog.isPublished()) {
			throw new BlogException("No such Blog Published or Exists");
		}
		
		Comment comment = commentRepository.findById(commentId).orElse(null);

		
		if((comment == null) || (comment.getBlog().getId() != blogId)) {
			throw new BlogException("No such Comment exists on this post with this id "+ commentId);
		}
		
		blog.removeComment(comment);
		comment.setBlog(null);
		commentRepository.delete(comment);
		
	    return convertToBlogDTO(blog);
	}


	

	@Override
	public BlogDTO updateCommentToPost(int blogId, @Valid CommentDTO commentDTO) {
		Blog blog = blogRepository.findById(blogId).orElse(null);
		
		if(blog == null || !blog.isPublished()) {
			throw new BlogException("No such Blog Published or Exists");
		}
		
		Comment comment = commentRepository.findById(commentDTO.getId()).orElse(null);

		
		if((comment == null) || (comment.getBlog().getId() != blogId)) {
			throw new BlogException("No such Comment exists on this post with this id "+ commentDTO.getId());
		}
		comment.setDescription(commentDTO.getDescription());
		commentRepository.save(comment);
		return convertToBlogDTO(blog);
	}


	
	
	
	
	

	
	private List<BlogDTO> convertListToDTO(List<Blog> allBlogs) {
		List<BlogDTO> blogs = new ArrayList<>();
		for(Blog b:allBlogs) {
			blogs.add(convertToBlogDTO(b));
		}
		return blogs;
	}
	
	
	private BlogDTO convertToBlogDTO(Blog blog) {
		BlogDTO blogDTO = new BlogDTO();
		blogDTO.setId(blog.getId());
	    blogDTO.setTitle(blog.getTitle());
	    blogDTO.setCategory(blog.getCategory());
	    blogDTO.setData(blog.getData());
	    blogDTO.setPublishedDate(blog.getPublishedDate());
	    blogDTO.setPublished(blog.isPublished());
	    if(blog.getComments() != null) blogDTO.setComments(convertToListCommentDTO(blog.getComments()));
        return blogDTO;
    }
	
	
	private List<CommentDTO> convertToListCommentDTO(List<Comment> comments) {
		List<CommentDTO> allComments = new ArrayList<>();
		for(Comment c:comments) {
			allComments.add(convertToCommentDTO(c));
		}
		return allComments;
	}
	
	
	private CommentDTO convertToCommentDTO(Comment comment) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment.getId());
		commentDTO.setDescription(comment.getDescription());
		return commentDTO;
	}
	
	
	private Blog convertToBlogEntity(BlogDTO blogDTO) {
		Blog blog = new Blog();
	    blog.setId(blogDTO.getId());
	    blog.setTitle(blogDTO.getTitle());
	    blog.setCategory(blogDTO.getCategory());
	    blog.setData(blogDTO.getData());
	    blog.setPublishedDate(blogDTO.getPublishedDate());
	    blog.setPublished(blogDTO.isPublished());
	    if(blogDTO.getComments() != null)blog.setComments(convertToListCommentEntity(blogDTO.getComments(),blog));
	    return blog;
	}
	
	
	
	private List<Comment> convertToListCommentEntity(List<CommentDTO> allCommentsDTO, Blog blog) {
		List<Comment> allComments = new ArrayList<>();
		for(CommentDTO c:allCommentsDTO) {
			allComments.add(convertToCommentEntity(c, blog));
		}
		return allComments;
	}

	
	private Comment convertToCommentEntity(CommentDTO commentDTO, Blog blog) {
		Comment comment = new Comment();
		comment.setId(commentDTO.getId());
		comment.setDescription(commentDTO.getDescription());
		comment.setBlog(blog);
		return comment;
	}





}
