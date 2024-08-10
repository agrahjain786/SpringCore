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

@Entity
@Table(name = "blog")
public class Blog {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
    private int id;
	
	@Column(name ="title")
    private String title;
    
	@Column(name ="category")
    private String category;
    
	@Column(name ="data")
    private String data;
    
	@Column(name ="published_date")
    private LocalDateTime publishedDate;
    
	@Column(name ="published")
    private boolean published;
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "blog")
	@JsonManagedReference
    private List<Comment> courses;
}
