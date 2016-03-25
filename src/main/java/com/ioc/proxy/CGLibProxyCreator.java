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

import net.sf.cglib.proxy.CallbackFilter;
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
<<<<<<< HEAD
	static Map<String, Object> globalInjectedMap = new HashMap<String, Object>();
=======
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
	static Map<String, Object> globalSingletonMap = new HashMap<String, Object>();

	ParseClasses parser;
	ClassLoader contextClassLoader;

	public CGLibProxyCreator() {
		parser = new ParseClasses();
		contextClassLoader = Thread.currentThread().getContextClassLoader();
	}

	public void loadSingletons(String packageName) throws IOException,
			ClassNotFoundException, InstantiationException,
<<<<<<< HEAD
			IllegalAccessException, SecurityException, NoSuchFieldException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
		List<Class> classes = parser
				.getClasses(contextClassLoader, packageName);
		for (Class<?> className : classes) {
			loadSingletonProxy(className);
		}
		for (Class<?> className : classes) {
			if (className.isAnnotationPresent(Singleton.class)) {
			System.out.println("-------------------------------------------------");
			System.out.println("LOAD STEP 2 (class) "+className.getSimpleName());
			System.out.println("Load Dependencies");
			injectDependencies(className);
			System.out.println("-------------------------------------------------");
		}
		}
		for (Class<?> className : classes) {
			if (className.isAnnotationPresent(Singleton.class)) {
			System.out.println("Load Proxy Step 2");
			createSingletonProxy(className, true);
			System.out.println("-------------------------------------------------");
			System.out.println("MAP: "+getGlobalSingletonMap());
			System.out.println("-------------------------------------------------");
		}}
	}

	private void loadSingletonProxy(Class<?> annotatedClass)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
//		System.out.println("CLASS: " + annotatedClass);
		Annotation[] annotations = annotatedClass.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Singleton) {
				//System.out.println("Has annotation " + annotatedClass);
				// TODO annotatedClass.newInstance() change to proxy
				Object createSingletonProxy = createSingletonProxy(annotatedClass,false);
				getGlobalSingletonMap().put(annotatedClass.getSimpleName(),
						createSingletonProxy);
				// injectDependencies(annotatedClass);
				//System.out.println(getGlobalSingletonMap());
			}
		}
		

	}

	@SuppressWarnings("unchecked")
	public Object createSingletonProxy(Class<?> className, boolean secondLoad)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		System.out.println("CLASS: "+className);
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(className);
		final Object newInstance = className.newInstance();
		//System.out.println("Create singleton proxy ");
		enhancer.setCallbackTypes(new Class[] { MethodInterceptor.class,
				NoOp.class });
		enhancer.setCallbackFilter(new CallbackFilter() {
			/** ignores bridge methods */
			public int accept(Method method) {
				return method.isBridge() ? 1 : 0;
			}
		});
//		Object newInstance2 = className.newInstance();
		Constructor<?> constructor = className.getConstructor();
		Object newInstance3 = constructor.newInstance();
		
		if(secondLoad){
			
		Field[] fields = className.getFields();
		for(Field field: fields){
			field.setAccessible(true);
			Inject annotation = field.getAnnotation(Inject.class);
//			System.out.println("annotation: "+annotation.service());
//			System.out.println("field: "+field.getType());
//			System.out.println("obj: "+newInstance2.toString());
			field.set(newInstance3, getGlobalSingletonMap().get(annotation.service()));
			Field field2 = newInstance3.getClass().getField(annotation.service());
			
			Class<?> type = field2.getType();
			Object object = getGlobalSingletonMap().get(annotation.service());
			System.out.println("FIELD SIMPLE NAME: "+type.getCanonicalName());
			for(Field f: type.getFields()){
				if(f.getType().equals(className)){
					f.set(object, getGlobalSingletonMap().get(className.getSimpleName()));
					Field field3 = object.getClass().getField(className.getSimpleName());
					Class<?> type2 = field3.getType();
					for(Field f2: type2.getFields()){
						Object object2 = getGlobalSingletonMap().get(className.getSimpleName());
					
						if(f2.getType().getSimpleName().equals(annotation.service())){
							f2.set(object2, getGlobalSingletonMap().get(annotation.service()));
						}}
				}
				System.out.println("TYPEEEE "+f.getType());
			}
			field.set(newInstance3, getGlobalSingletonMap().get(annotation.service()));
//			Field field3 = type.getClass().getField(className.getSimpleName());
//			if(field3!=null){
//				System.out.println("HAS Field");
//			}
			getGlobalSingletonMap().put(className.getSimpleName(), newInstance3);
			System.out.println("MAP(createSingletonProxy2 UPDATED): "+getGlobalSingletonMap());
		}
		}
		
		return newInstance3;
	}

	public void injectDependencies(Class<?> className)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = className.getFields();
		Object object = getGlobalSingletonMap().get(className.getSimpleName());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Inject.class)) {
				Inject annotation = field.getAnnotation(Inject.class);
				System.out.println("annotation1: "+annotation.service());
				System.out.println("field1: "+field.getType());
				System.out.println("obj1: "+object.toString());
//				System.out.println("Inject to class " + object);
//				System.out.println("Field  " + field.getAnnotation(Inject.class));
				field.set(
						object,
						getGlobalSingletonMap().get(
								field.getAnnotation(Inject.class)));
			}
		}
		
		getGlobalBeansMap().put(className.getSimpleName(), object);
		System.out.println("MAP(injectDependencies1): "+getGlobalSingletonMap());
//		System.out.println("Final Map: "+getGlobalSingletonMap());
		
=======
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
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
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
<<<<<<< HEAD
		System.out.println("CLASS NAME: " + className.getName());
		createProxy(className);
		System.out.println("INJECTED MAP: " + getGlobalInjectedMap());

	}

	public void loadInjectedPackageClasses(String packageName)
			throws IOException, ClassNotFoundException, InstantiationException,
=======
		createProxy(className);

	}

	public void loadPackageBeanClasses(String packageName) throws IOException,
			ClassNotFoundException, InstantiationException,
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
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
<<<<<<< HEAD
				System.out.println("Name: " + name);
				// TODO annotatedClass.newInstance() change to proxy
				T createProxy = createProxy(annotatedClass);
				System.out.println("created proxy " + createProxy);
=======
				T createProxy = createProxy(annotatedClass);
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
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
<<<<<<< HEAD
		// System.out.println("Global map: " + globalBeansMap);
=======
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
	}

	@SuppressWarnings("unchecked")
	public T createProxy(Class<?> className) throws ClassNotFoundException {
<<<<<<< HEAD
		System.out.println("create proxy " + className);
=======
>>>>>>> b34c47e21f223c9dc852e48bbf12967908823bb3
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(className);
		enhancer.setCallback(new MethodInterceptor() {

			public Object intercept(Object obj, Method method, Object[] args,
					MethodProxy proxy) throws Throwable {
				Field[] fields = obj.getClass().getFields();
				System.out.println("in fields");
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

//	@SuppressWarnings("unchecked")
//	public T createSingleProxy(final Class<?> className)
//			throws ClassNotFoundException {
//		System.out.println("create proxy single " + className);
//		Enhancer enhancer = new Enhancer();
//		enhancer.setSuperclass(className);
//		enhancer.setCallback(new FixedValue() {
//
//			public Object loadObject() throws Exception {
//				// TODO Auto-generated method stub
//				return className.newInstance();
//			}
//		});
//		return (T) enhancer.create();
//	}

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

	public static Map<String, Object> getGlobalSingletonMap() {
		return globalSingletonMap;
	}

	public static void setGlobalSingletonMap(
			Map<String, Object> globalSingletonMap) {
		CGLibProxyCreator.globalSingletonMap = globalSingletonMap;
	}

}
