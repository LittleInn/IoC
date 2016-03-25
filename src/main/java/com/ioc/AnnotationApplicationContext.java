package com.ioc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.ioc.proxy.CGLibProxyCreator;

public class AnnotationApplicationContext {
	private CGLibProxyCreator cglibCreator;
	
	public AnnotationApplicationContext() {
		 cglibCreator = new CGLibProxyCreator();
	}
	public void register(String packageName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchFieldException, NoSuchMethodException{
//		cglibCreator.loadInjectedPackageClasses(packageName);
		//cglibCreator.loadBeanClasses();
//		cglibCreator.loadProvidedPackageClass(packageName);
		cglibCreator.loadSingletons(packageName);
	}
	public void register(Class<?> ... classes){
		
	}
	private void loadProxies() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		cglibCreator.loadBeanClasses();
		cglibCreator.loadInjectedClasses();
	}
	
	public Object getBean(String beanName){
		return cglibCreator.getGlobalSingletonMap().get(beanName);
//		return cglibCreator.getGlobalBeansMap().get(beanName);
	}
}
