package com.ioc.proxy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import com.ioc.ParseClasses;
import com.ioc.annotations.Bean;
import com.ioc.annotations.Inject;
import com.ioc.annotations.Provided;
import com.ioc.annotations.Singleton;

public class CGLibProxyCreator<T> {

	static List<String> packages = new ArrayList<String>(Arrays.asList(
			"com/ioc", "com/ioc/model", "com/ioc/annotations", "com/ioc/impl",
			"com/ioc/service", "com/ioc/usage"));

	static Map<String, Object> globalBeansMap = new HashMap<String, Object>();
	static Map<String, Object> globalSingletonMap = new HashMap<String, Object>();

	ParseClasses parser;
	ClassLoader contextClassLoader;

	public CGLibProxyCreator() {
		parser = new ParseClasses();
		contextClassLoader = Thread.currentThread().getContextClassLoader();
	}

	public void loadSingletons(String packageName) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchFieldException,
			IllegalArgumentException, NoSuchMethodException,
			InvocationTargetException {
		List<Class> classes = parser
				.getClasses(contextClassLoader, packageName);
		initSingletonMapHolder(classes);
		createSingletonObjects(classes);
	}

	private void initSingletonMapHolder(List<Class> classes)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SecurityException, NoSuchFieldException,
			IllegalArgumentException, NoSuchMethodException,
			InvocationTargetException {
		for (Class<?> annotatedClass : classes) {
			Annotation[] annotations = annotatedClass.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof Singleton) {
					getGlobalSingletonMap().put(annotatedClass.getSimpleName(),
							loadInitialSingletons(annotatedClass));
				}
			}
		}

	}

	private Object loadInitialSingletons(Class<?> annotatedClass) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(annotatedClass);
		enhancer.setCallback(NoOp.INSTANCE);
		return enhancer.create();

	}

	private void createSingletonObjects(List<Class> classes)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchFieldException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {
		for (Class<?> annotatedClass : classes) {
			Annotation[] annotations = annotatedClass.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof Singleton) {
					Object createSingletonProxy = createSingletonProxy(annotatedClass);
					getGlobalSingletonMap().put(annotatedClass.getSimpleName(),
							createSingletonProxy);
				}
			}
			getGlobalBeansMap().putAll(getGlobalSingletonMap());
		}
	}

	@SuppressWarnings("unchecked")
	public Object createSingletonProxy(Class<?> className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchFieldException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(className);
		enhancer.setCallback(NoOp.INSTANCE);

		Object newInstance = null;

		Constructor<?>[] constructors = className.getConstructors();
		for (Constructor<?> c : constructors) {
			if (c.getParameterTypes().length > 0) {
				Class<?> constructorParameterClass = c.getParameterTypes()[0];
				Constructor<?> constructor = className.getConstructor(c
						.getParameterTypes()[0]);
				newInstance = constructor.newInstance(getGlobalSingletonMap()
						.get(constructorParameterClass.getSimpleName()));
			}
		}
		return newInstance;
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
		createProxy(className);

	}

	public void loadPackageBeanClasses(String packageName) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		List<Class> classes = parser
				.getClasses(contextClassLoader, packageName);
		for (Class<?> className : classes) {
			loadBeanProxy(className);
		}
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
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Annotation[] annotations = annotatedClass.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Bean) {
				String name = ((Bean) annotation).name();
				T createProxy = createProxy(annotatedClass);
				getGlobalBeansMap().put(name, createProxy);
			}

		}
	}

	public void loadProvidedPackageClass(String packageName)
			throws ClassNotFoundException, IOException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		List<Class> classes = parser
				.getClasses(contextClassLoader, packageName);
		for (Class<?> className : classes) {
			loadProvided(className);
		}

	}

	public void loadProvided(Class<?> className) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Method[] methods = className.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Provided.class)) {
				Provided annotation = method.getAnnotation(Provided.class);
				String name = ((Provided) annotation).name();
				getGlobalBeansMap().put(name,
						method.invoke(className.newInstance()));

			}
		}
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
					if (field.isAnnotationPresent(Inject.class)) {

						field.setAccessible(true);
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
		return (T) enhancer.create();
	}

	public static Map<String, Object> getGlobalBeansMap() {
		return globalBeansMap;
	}

	public static void setGlobalBeansMap(Map<String, Object> globalBeansMap) {
		CGLibProxyCreator.globalBeansMap = globalBeansMap;
	}

	public static Map<String, Object> getGlobalSingletonMap() {
		return globalSingletonMap;
	}

	public static void setGlobalSingletonMap(
			Map<String, Object> globalSingletonMap) {
		CGLibProxyCreator.globalSingletonMap = globalSingletonMap;
	}

}
