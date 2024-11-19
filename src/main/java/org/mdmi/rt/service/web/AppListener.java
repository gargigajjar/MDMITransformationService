package org.mdmi.rt.service.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		System.setProperty(
			"javax.xml.transform.TransformerFactory",
			"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
	}

}
