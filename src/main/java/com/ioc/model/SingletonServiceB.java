package com.ioc.model;

import com.ioc.annotations.Inject;
import com.ioc.annotations.Singleton;

@Singleton
public class SingletonServiceB {
	
	@Inject(service = "SingletonServiceA")
	public SingletonServiceA SingletonServiceA; 
	
	public void createB(){
		System.out.println("create B");
		SingletonServiceA.createA();
	}
}
