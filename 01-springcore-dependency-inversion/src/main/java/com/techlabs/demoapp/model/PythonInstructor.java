package com.techlabs.demoapp.model;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class PythonInstructor implements Instructor{
	
	private Resource resource;
	
	public PythonInstructor(@Qualifier(value="videoResource")Resource resource) {
		super();
		this.resource = resource;
	}

	@Override
	public String getTrainingPlan() {
		// TODO Auto-generated method stub
		return "Practicing Linear regression" + resource.getResourceMaterial();
	}

	

}
