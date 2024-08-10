package com.techlabs.demoapp.model;

import org.springframework.stereotype.Component;

@Component
public class PdfResource implements Resource{

	@Override
	public String getResourceMaterial() {
		// TODO Auto-generated method stub
		return "pdf resource";
	}

}