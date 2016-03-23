package com.ioc;

public class ApplicationContext implements AbstractApplicationContext{

	public XmlApplicationContext createXmlApplicationContext(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	public AnnotationApplicationContext createAnnotationApplicationContext() {
		return new AnnotationApplicationContext();
	}

}
