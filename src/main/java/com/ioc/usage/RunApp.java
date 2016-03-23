package com.ioc.usage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.ioc.AnnotationApplicationContext;
import com.ioc.model.User;
import com.ioc.service.CompanyService;

public class RunApp {
	public RunApp() {

	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		AnnotationApplicationContext context = new AnnotationApplicationContext();
		context.register("com/ioc/impl");
		CompanyService bean = (CompanyService) context
				.getBean("companyServiceImpl");
		bean.createCompany("test inn company");
		context.register("com/ioc/model");
		User user = (User) context.getBean("user");
		user.setName("Main User");
		System.out.println("User name: "+user.getName());
	}
}
