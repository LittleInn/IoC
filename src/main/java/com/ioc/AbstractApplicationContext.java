package com.ioc;

public interface AbstractApplicationContext {
	XmlApplicationContext createXmlApplicationContext(String file);

	AnnotationApplicationContext createAnnotationApplicationContext();
}
