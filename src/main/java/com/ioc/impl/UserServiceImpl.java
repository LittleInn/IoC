package com.ioc.impl;

import com.ioc.annotations.Bean;
import com.ioc.model.User;
import com.ioc.service.UserService;

@Bean(name = "userServiceImpl")
public class UserServiceImpl implements UserService {

	public UserServiceImpl() {
	}

	public boolean createUser(String name) {
		User user = new User("Test");
		System.out.println("user created: "+name);
		return (user != null) ? true : false;
	}

}
