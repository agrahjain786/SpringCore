package com.techlabs.app.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.BlogDTO;
import com.techlabs.app.dto.CommentDTO;
import com.techlabs.app.service.BlogService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Blog", description = "Blog related Endpoints/APIs")
@RestController
@RequestMapping("/api/blog")
public class BlogController {
	
	
	private BlogService blogService;

	public BlogController(BlogService blogService) {
		super();
		this.blogService = blogService;
	}
	
	
	@Operation(
		      summary = "Retrieve All the Blogs",
		      description = "Get the paginated list of Blogs",
		      tags = { "Blogs", "Get" })
//	@ApiResponses({
//	      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PagedResponse.class), mediaType = "application/json") }),
//	      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
//	      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
	@GetMapping
	public ResponseEntity<PagedResponse<BlogDTO>> getAllBlogs(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<BlogDTO> blogs = blogService.getAllBlogs(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<BlogDTO>>(blogs, HttpStatus.OK);
		
	}
	
	
	@Operation(
		      summary = "Retrieve the Blog by ID",
		      description = "Get the particular blog with comments if found",
		      tags = { "Blogs", "Get" })
	@GetMapping("/{id}")
	public ResponseEntity<BlogDTO> getBlogById(@PathVariable(name="id")int blogId){
		BlogDTO blogDTO = blogService.getBlogById(blogId);
		
		return new ResponseEntity<BlogDTO>(blogDTO, HttpStatus.OK);
		
	}
	
	
	@Operation(
		      summary = "Add a Blog",
		      description = "Adding the Blog if it is valid otherwise not",
		      tags = { "Blogs", "Post" })
	@PostMapping
	public ResponseEntity<BlogDTO> addBlog(@Valid @RequestBody BlogDTO blogDTO){
		BlogDTO addedBlog = blogService.addBlog(blogDTO);
		
		return new ResponseEntity<BlogDTO>(addedBlog, HttpStatus.CREATED);
		
	}
	
	
	@Operation(
		      summary = "Update the Blog",
		      description = "Update the Validated Blog",
		      tags = { "Blogs", "Put" })
	@PutMapping
	public ResponseEntity<BlogDTO> updateBlog(@Valid @RequestBody BlogDTO blogDTO){
		BlogDTO updatedBlog = blogService.updateBlog(blogDTO);
		
		return new ResponseEntity<BlogDTO>(updatedBlog, HttpStatus.OK);
		
	}
	
	
	@Operation(
		      summary = "Delete a Blog",
		      description = "Delete the Blog by ID if found",
		      tags = { "Blogs", "Delete" })
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBlog(@PathVariable(name="id")int blogId){
		blogService.deleteBlog(blogId);
		
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
		
	}
	
	
	@Operation(
		      summary = "Get All Published Blog",
		      description = "Retrieve all published Blogs",
		      tags = { "Blogs", "Get" })
	@GetMapping("/all/published")
	public ResponseEntity<PagedResponse<BlogDTO>> getAllPublishedBlogs(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<BlogDTO> blogs = blogService.getAllPublishedBlogs(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<BlogDTO>>(blogs, HttpStatus.OK);
		
	}
	
	
	@Operation(
		      summary = "Get All Unpublished Blog",
		      description = "Retrieve all unpublished Blogs",
		      tags = { "Blogs", "Get" })
	@GetMapping("/all/unpublished")
	public ResponseEntity<PagedResponse<BlogDTO>> getAllUnpublishedBlogs(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name = "direction", defaultValue = "asc") String direction){
		
		PagedResponse<BlogDTO> blogs = blogService.getAllUnpublishedBlogs(page,size,sortBy,direction);
		
		return new ResponseEntity<PagedResponse<BlogDTO>>(blogs, HttpStatus.OK);
		
	}
	
	
	
	@Operation(
		      summary = "Make the blog published",
		      description = "Make the Blog Published if not",
		      tags = { "Blogs", "Put" })
	@PutMapping("/published/{id}")
	public ResponseEntity<BlogDTO> makeBlogPublished(@PathVariable(name="id")int blogId){
		BlogDTO updatedBlogDTO = blogService.makeBlogPublished(blogId);
		
		return new ResponseEntity<BlogDTO>(updatedBlogDTO, HttpStatus.OK);
		
	}
	
	
	
	@Operation(
		      summary = "Add Comment",
		      description = "Add Comment to the Available Blog post",
		      tags = { "Comment", "Post" })
	@PostMapping("/{id}/comment")
	public ResponseEntity<BlogDTO> addCommentToPost(@PathVariable(name="id")int blogId, @Valid @RequestBody CommentDTO commentDTO){
		BlogDTO updatedBlogDTO = blogService.addCommentToPost(blogId, commentDTO);
		
		return new ResponseEntity<BlogDTO>(updatedBlogDTO, HttpStatus.OK);
		
	}
	
	
	
	@Operation(
		      summary = "Delete Comment from blog",
		      description = "Delete Comment to the Available Blog post",
		      tags = { "Comment", "Delete" })
	@DeleteMapping("/{id}/comment/{cid}")
	public ResponseEntity<BlogDTO> deleteCommentToPost(@PathVariable(name="id")int blogId, @PathVariable(name="cid")int commentId){
		BlogDTO updatedBlogDTO = blogService.deleteCommentToPost(blogId, commentId);
		
		return new ResponseEntity<BlogDTO>(updatedBlogDTO, HttpStatus.OK);
		
	}
	
	
	@Operation(
		      summary = "Update Comment from blog",
		      description = "update Comment to the Available Blog post",
		      tags = { "Comment", "Put" })
	@PutMapping("/{id}/comment")
	public ResponseEntity<BlogDTO> updateCommentToPost(@PathVariable(name="id")int blogId,@Valid @RequestBody CommentDTO commentDTO){
		BlogDTO updatedBlogDTO = blogService.updateCommentToPost(blogId, commentDTO);
		
		return new ResponseEntity<BlogDTO>(updatedBlogDTO, HttpStatus.OK);
		
	}
	
	
	
}
