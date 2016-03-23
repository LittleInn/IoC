package com.ioc.usage;

import com.ioc.annotations.Inject;
import com.ioc.service.CompanyService;
import com.ioc.service.UserService;

public class App1 {
	
	@Inject(service = "userServiceImpl")
	public UserService userServiceImpl;
	
	@Inject(service = "companyServiceImpl")
	public CompanyService companyServiceImpl;
	
	public App1() {
	}
	public  void main() {
		
		userServiceImpl.createUser("Test 1");
		companyServiceImpl.createCompany("New Company");
		companyServiceImpl.deleteCompany("New Company");
	}
}
