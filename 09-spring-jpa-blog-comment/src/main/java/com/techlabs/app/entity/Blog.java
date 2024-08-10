package com.techlabs.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog")
public class Blog {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
    private int id;
	
	@NotBlank
	@Size(max = 100)
	@Column(name ="title")
    private String title;
    
	@NotBlank
	@Size(max = 255)
	@Column(name ="category")
    private String category;
    
	@NotBlank
	@Column(name ="data")
    private String data;
    
	@Column(name ="published_date")
    private LocalDateTime publishedDate;
    
	@Column(name ="published")
    private boolean published;
	
	@OneToMany(mappedBy = "blog", cascade = CascadeType.ALL,orphanRemoval = true)
	@JsonManagedReference
    private List<Comment> comments;

	public void addComment(Comment comment) {
		comments.add(comment);
	}

	public void removeComment(Comment comment) {
		this.comments.remove(comment);
		
	}
}
