package com.ioc.model;


import com.ioc.annotations.Singleton;

@Singleton
public class SingletonServiceB {

	//@Inject(service = "SingletonServiceA")
	public SingletonServiceA ingletonServiceA;

	public SingletonServiceB() {
	}

	public SingletonServiceB(SingletonServiceA ingletonServiceA) {
		this.ingletonServiceA = ingletonServiceA;
		System.out.println("this.SingletonServiceA" + ingletonServiceA);
	}

	public void createB() {
		System.out.println("create B");
		// SingletonServiceA.createA();
	}
}

