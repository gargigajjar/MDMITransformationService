/*******************************************************************************
 * Copyright (c) 2020 MDIX, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompany this distribution and is available at 
 * https\://www.apache.org/licenses/LICENSE-2.0.
 *
 * Contributors:
 *     seanmuir - initial API and implementation
 *
 *******************************************************************************/
package org.mdmi.rt.service.web;

/**
 * @author seanmuir
 *
 */

public class FHIRTerminologySettings {

	/**
	 *
	 */
	public FHIRTerminologySettings() {
		super();
		url = "http://terminologyservice.mdixinc.net:8080/fhir";
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	private String url;

	private String userName;

	private String password;

}
