/*******************************************************************************
 * Copyright (c) 2020 seanmuir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     seanmuir - initial API and implementation
 *
 *******************************************************************************/
package org.mdmi.rt.service.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author seanmuir
 *
 */

@Component
@ConfigurationProperties("mdmi")
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
