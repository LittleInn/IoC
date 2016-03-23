package com.ioc.model;

import com.ioc.annotations.Provided;

public class User {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User() {
	}

	public User(String name) {
		super();
		this.name = name;
	}

	@Provided(name="user")
	public User getInstance() {
		System.out.println("User instance is created");
		return new User();
	}

}
