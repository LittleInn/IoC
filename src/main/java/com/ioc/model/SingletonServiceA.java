package com.ioc.model;

import com.ioc.annotations.Singleton;

@Singleton
public class SingletonServiceA {

	//@Inject(service = "SingletonServiceB")
	public SingletonServiceB singletonServiceB;

	public SingletonServiceA() {
	}

	public SingletonServiceA(SingletonServiceB SingletonServiceB) {
		this.singletonServiceB = SingletonServiceB;
	}

	public void createA() {
		System.out.println("create A");
		singletonServiceB.createB();
	}
}
