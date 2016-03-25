package com.ioc.proxy;


public class SingletonCreator<T> {
private SingletonCreator<T> instance = new SingletonCreator<T>();

	public synchronized SingletonCreator getInstance() {
		return instance;
	}
//	public void createInstance(Class<?> classInstance ,T t) throws InstantiationException, IllegalAccessException{
//		T newInstance = (T)classInstance.newInstance();
//		if(cla)
//	}
}
