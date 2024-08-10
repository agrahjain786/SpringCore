package com.techlabs.demoapp.model;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class VideoResource implements Resource{

	@Override
	public String getResourceMaterial() {
		// TODO Auto-generated method stub
		return "video resource";
	}

}