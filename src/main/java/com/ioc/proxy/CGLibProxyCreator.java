package com.ioc.proxy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.ioc.ParseClasses;
import com.ioc.annotations.Bean;
import com.ioc.annotations.Inject;

public class CGLibProxyCreator<T> { 

	static List<String> packages = new ArrayList<String>(Arrays.asList(
			"com/ioc", "com/ioc/model", "com/ioc/annotations", "com/ioc/impl",
			"com/ioc/service", "com/ioc/usage"));

	static Map<String, Object> globalBeansMap = new HashMap<String, Object>();
	static Map<String, Object> globalInjectedMap = new HashMap<String, Object>();

	boolean injectPresent = false;

	ParseClasses parser;
	ClassLoader contextClassLoader;

	public CGLibProxyCreator() {
		parser = new ParseClasses();
		contextClassLoader = Thread.currentThread().getContextClassLoader();
	}

	public void loadInjectedClasses() throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		for (String packageName : packages) {
			List<Class> classes = parser.getClasses(contextClassLoader,
					packageName);
			for (Class<?> className : classes) {
				loadInjectedProxy(className);
			}
		}
	}

	private void loadInjectedProxy(Class<?> className)
			throws ClassNotFoundException {
		System.out.println("CLASS NAME: " + className.getName());
		createProxy(className);
		System.out.println("INJECTED MAP: " + getGlobalInjectedMap());

	}

	public void loadBeanClasses() throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		for (String packageName : packages) {
			List<Class> classes = parser.getClasses(contextClassLoader,
					packageName);
			for (Class<?> className : classes) {
				loadBeanProxy(className);
			}
		}
	}

	private void loadBeanProxy(Class<?> annotatedClass)
			throws InstantiationException, IllegalAccessException {
		Annotation[] annotations = annotatedClass.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Bean) {
				String name = ((Bean) annotation).name();
				System.out.println("Name: " + name);
				// TODO annotatedClass.newInstance() change to proxy
				getGlobalBeansMap().put(name, annotatedClass.newInstance());
			}
		}
		System.out.println("Global map: " + globalBeansMap);
	}

	@SuppressWarnings("unchecked")
	public T createProxy(Class<?> className) throws ClassNotFoundException {

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(className);
		enhancer.setCallback(new MethodInterceptor() {

			public Object intercept(Object obj, Method method, Object[] args,
					MethodProxy proxy) throws Throwable {
				Field[] fields = obj.getClass().getFields();
				for (Field field : fields) {
					System.out.println("FIELD: "+field.getType());
					if (field.isAnnotationPresent(Inject.class)) {
						System.out.println("has annotation: ");
						injectPresent = true;
						field.setAccessible(true);
						System.out.println("MAP: "+getGlobalBeansMap());
						System.out.println("Annotation: "+getGlobalBeansMap().get(
								field.getAnnotation(Inject.class)
								.service()));
						field.set(
								obj,
								getGlobalBeansMap().get(
										field.getAnnotation(Inject.class)
												.service()));
					}
				}
				return proxy.invokeSuper(obj, args);
			}
		});
//		if (injectPresent) {
//			getGlobalInjectedMap().put(className.getSimpleName(),
//					(T) enhancer.create());
//		}
		return (T) enhancer.create();
	}

	public static Map<String, Object> getGlobalBeansMap() {
		return globalBeansMap;
	}

	public static void setGlobalBeansMap(Map<String, Object> globalBeansMap) {
		CGLibProxyCreator.globalBeansMap = globalBeansMap;
	}

	public static Map<String, Object> getGlobalInjectedMap() {
		return globalInjectedMap;
	}

	public static void setGlobalInjectedMap(
			Map<String, Object> globalInjectedMap) {
		CGLibProxyCreator.globalInjectedMap = globalInjectedMap;
	}

}
