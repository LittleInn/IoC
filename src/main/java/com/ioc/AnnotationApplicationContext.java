package com.ioc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.ioc.proxy.CGLibProxyCreator;

public class AnnotationApplicationContext {
	private CGLibProxyCreator cglibCreator;

	public AnnotationApplicationContext() {
		cglibCreator = new CGLibProxyCreator();
	}
<<<<<<< HEAD
	public void register(String packageName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchFieldException, NoSuchMethodException{
//		cglibCreator.loadInjectedPackageClasses(packageName);
		//cglibCreator.loadBeanClasses();
//		cglibCreator.loadProvidedPackageClass(packageName);
		cglibCreator.loadSingletons(packageName);
=======

	public void register(String packageName) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException,
			IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchFieldException, NoSuchMethodException {
		cglibCreator.loadSingletons(packageName);
		cglibCreator.loadBeanClasses();
		cglibCreator.loadProvidedPackageClass(packageName);
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
	}

	public void register(Class<?>... classes) {

	}

	private void loadProxies() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException {
		cglibCreator.loadBeanClasses();
		cglibCreator.loadInjectedClasses();
	}
<<<<<<< HEAD
	
	public Object getBean(String beanName){
		return cglibCreator.getGlobalSingletonMap().get(beanName);
//		return cglibCreator.getGlobalBeansMap().get(beanName);
=======

	public Object getBean(String beanName) {
		return cglibCreator.getGlobalBeansMap().get(beanName);
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
	}
}
