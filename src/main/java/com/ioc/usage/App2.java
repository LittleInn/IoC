package com.ioc.usage;

import com.ioc.annotations.Inject;
import com.ioc.service.CompanyService;
import com.ioc.service.UserService;

public class App2 {

	@Inject(service = "userServiceImpl")
	public UserService userService;

	@Inject(service = "companyServiceImpl")
	public CompanyService companyService;

	public App2() {
	}

	public void main() {
		System.out.println("user service: " + userService);
		userService.createUser("Test 2");
	}
}
