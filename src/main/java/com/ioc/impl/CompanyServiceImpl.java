package com.ioc.impl;

import com.ioc.annotations.Bean;
import com.ioc.service.CompanyService;

@Bean(name = "companyServiceImpl")
public class CompanyServiceImpl implements CompanyService {

	public void createCompany(String name) {
		System.out.println("company created: " + name);
	}

	public void deleteCompany(String name) {
		System.out.println("company deleted: " + name);
	}

}
