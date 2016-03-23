package com.ioc.usage;

import java.io.IOException;

import com.ioc.proxy.CGLibProxyCreator;

public class RunApp {
	public RunApp() {
		
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		CGLibProxyCreator c = new CGLibProxyCreator();
		c.loadBeanClasses();
		c.loadInjectedClasses();
		App1 app1 = (App1)c.createProxy(App1.class);
		app1.main();
	}
}
