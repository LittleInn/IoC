package com.ioc.usage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.ioc.AnnotationApplicationContext;
import com.ioc.model.SingletonServiceA;
import com.ioc.model.User;
import com.ioc.service.CompanyService;

public class RunApp {
	public RunApp() {

	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException, NoSuchFieldException, NoSuchMethodException {
		AnnotationApplicationContext context = new AnnotationApplicationContext();
		context.register("com/ioc/impl");
		context.register("com/ioc/model");
		CompanyService bean = (CompanyService) context
				.getBean("companyServiceImpl");
		bean.createCompany("test inn company");
		User user = (User) context.getBean("user");
		user.setName("Main User");
		System.out.println("User name: "+user.getName());
		SingletonServiceA sa = (SingletonServiceA)context.getBean("SingletonServiceA");
		System.out.println("sa: "+sa);
		sa.createA();
	}
}
