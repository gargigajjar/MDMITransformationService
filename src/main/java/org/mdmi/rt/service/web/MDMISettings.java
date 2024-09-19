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

public class MDMISettings {

	private Boolean sourceFilterFlag;

	/**
	 * @return the sourceFilterFlag
	 */
	public Boolean getSourceFilterFlag() {
		return sourceFilterFlag;
	}

	/**
	 * @param sourceFilterFlag the sourceFilterFlag to set
	 */
	public void setSourceFilterFlag(Boolean sourceFilterFlag) {
		this.sourceFilterFlag = sourceFilterFlag;
	}

}
