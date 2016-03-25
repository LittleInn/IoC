<<<<<<< HEAD
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
=======
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
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
