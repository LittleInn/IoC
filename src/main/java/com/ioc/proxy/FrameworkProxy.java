package com.ioc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class FrameworkProxy implements InvocationHandler{
	
	private   Object realObject;

	public FrameworkProxy(Object realObject) {
		super();
		this.realObject = realObject;
	}

	public static Object newInstance(Object obj) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new FrameworkProxy(obj));
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		Object result= null;
		try{
			result = method.invoke(realObject, args);
		}catch(InvocationTargetException e){
			throw e.getTargetException();
		}finally{
		}
		return result;
	}

}
