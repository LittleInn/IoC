package com.ioc.model;

import com.ioc.annotations.Inject;
import com.ioc.annotations.Singleton;

@Singleton
public class SingletonServiceA {

	@Inject(service = "SingletonServiceB")
	public SingletonServiceB SingletonServiceB;

	public  void createA() {
		System.out.println("create A");
		SingletonServiceB.createB();
	}
}
